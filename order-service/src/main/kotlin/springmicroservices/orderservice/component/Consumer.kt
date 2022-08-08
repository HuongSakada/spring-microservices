package springmicroservices.orderservice.component

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import springmicroservices.orderservice.models.OrderModel

@Component
class Consumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["product-order"], groupId = "order")
    fun listenStore(order: OrderModel) {
        logger.info("Receive confirmation from store: {}", order)
    }
}