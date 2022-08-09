package springmicroservices.userservice.service

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import springmicroservices.userservice.config.ACCOUNT_TOPIC
import springmicroservices.userservice.helpers.enum.Order
import springmicroservices.userservice.models.OrderModel
import springmicroservices.userservice.repository.AccountRepository

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val orderTemplate: KafkaTemplate<Long, OrderModel>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun reserve(order: OrderModel) {
        val customer = accountRepository.findById(order.customerId).orElseThrow()
        val totalPrice = order.price * order.productCount

        if (totalPrice < customer.amountAvailable) {
            customer.amountReserved = customer.amountReserved.plus(totalPrice)
            customer.amountAvailable = customer.amountAvailable.minus(totalPrice)
            order.status = Order.StatusEnum.ACCEPT.id
        } else {
            order.status = Order.StatusEnum.REJECT.id
        }

        order.source = Order.SourceEnum.ACCOUNT.toString()
        accountRepository.save(customer)
        orderTemplate.send(ACCOUNT_TOPIC, order.id, order)
        logger.info("Reserved account for order: {}", order)
    }

    fun confirm(order: OrderModel) {
        val customer = accountRepository.findById(order.customerId).orElseThrow()
        val totalPrice = order.price * order.productCount

        if (order.status == Order.StatusEnum.CONFIRMED.id) {
            customer.amountReserved = customer.amountReserved.minus(totalPrice)
            accountRepository.save(customer)
        } else if (order.status == Order.StatusEnum.ROLLBACK.id && order.source != Order.SourceEnum.ACCOUNT.toString()) {
            customer.amountReserved = customer.amountReserved.minus(totalPrice)
            customer.amountAvailable = customer.amountAvailable.plus(totalPrice)
            accountRepository.save(customer)
        }
    }
}