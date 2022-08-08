package springmicroservices.storeservice.component

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import springmicroservices.storeservice.helpers.enum.Order
import springmicroservices.storeservice.models.OrderModel
import springmicroservices.storeservice.service.ProductService

@Component
class OrderConsumer(
    private val productService: ProductService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["order"], groupId = "stock")
    fun listenOrder(order: OrderModel) {
        logger.info("Store received incoming order: {}", order)
        if (order.status == Order.StatusEnum.OPEN.id) {
            productService.reserve(order)
        } else {
            productService.confirm(order)
        }
    }
}