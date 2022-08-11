package springmicroservices.storeservice.service

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import springmicroservices.storeservice.config.PRODUCT_TOPIC
import springmicroservices.storeservice.helpers.enum.Order
import springmicroservices.storeservice.models.OrderModel
import springmicroservices.storeservice.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val orderTemplate: KafkaTemplate<Long, OrderModel>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun reserve(order: OrderModel) {
        val product = productRepository.findById(order.productId).orElseThrow()

        if (order.status == Order.StatusEnum.OPEN.id) {
            if (order.productCount < product.availableItems) {
                product.reservedItems = product.reservedItems.plus(order.productCount)
                product.availableItems = product.availableItems.minus(order.productCount)
                order.status = Order.StatusEnum.ACCEPT.id
                productRepository.save(product)
            } else {
                order.status = Order.StatusEnum.REJECT.id
            }

            order.source = Order.SourceEnum.PRODUCT.toString()
            orderTemplate.send(PRODUCT_TOPIC, order.id, order)
            logger.info("Reserved product for order: {}", order)
        }
    }

    fun confirm(order: OrderModel) {
        val product = productRepository.findById(order.productId).orElseThrow()

        if (order.status == Order.StatusEnum.CONFIRMED.id) {
            product.reservedItems = product.reservedItems.minus(order.productCount)
            productRepository.save(product)
            logger.info("Confirmed: {}", order)
        } else if (order.status == Order.StatusEnum.ROLLBACK.id && order.source != Order.SourceEnum.PRODUCT.toString()) {
            product.reservedItems = product.reservedItems.minus(order.productCount)
            product.availableItems = product.availableItems.plus(order.productCount)
            productRepository.save(product)
            logger.info("Rollback: {}", order)
        }
    }
}