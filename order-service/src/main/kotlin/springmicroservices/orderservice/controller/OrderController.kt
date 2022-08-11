package springmicroservices.orderservice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springmicroservices.orderservice.config.ORDER_TOPIC
import springmicroservices.orderservice.helpers.enum.Order
import springmicroservices.orderservice.models.OrderModel
import springmicroservices.orderservice.service.OrderService
import kotlin.random.Random

@RestController
@RequestMapping("/")
class OrderController(
    private val orderTemplate: KafkaTemplate<Long, OrderModel>,
    private val orderService: OrderService

) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun create(@RequestBody order: OrderModel): OrderModel {
        order.id = Random.nextLong(1, 1000)
        order.status = Order.StatusEnum.OPEN.id;
        order.source = Order.SourceEnum.ORDER.toString()

        orderService.send(order)
        logger.info("Incoming order sent: {}", order)

        return order
    }

    @PostMapping("/message")
    fun post(@Validated @RequestBody order: OrderModel): ResponseEntity<Any> {
        return try {
            logger.info("Receiving incoming order")
            logger.info("Sending message to Kafka {}", order)

            val message: Message<OrderModel> = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, ORDER_TOPIC)
                .setHeader(KafkaHeaders.GROUP_ID, "order_group")
                .setHeader("X-Custom-Header", "Custom header here")
                .build()
            orderTemplate.send(message)

            logger.info("Message sent with success")
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            logger.error("Exception: {}", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to send the message")
        }
    }
}