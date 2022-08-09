package springmicroservices.userservice.component

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import springmicroservices.userservice.helpers.enum.Order
import springmicroservices.userservice.models.OrderModel
import springmicroservices.userservice.service.AccountService

@Component
class OrderConsumer(
    private val accountService: AccountService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["order"], groupId = "account")
    fun listenOrder(order: OrderModel) {
        logger.info("Account received incoming order: {}", order)
        if (order.status == Order.StatusEnum.OPEN.id) {
            accountService.reserve(order)
        } else {
            accountService.confirm(order)
        }
    }
}