package springmicroservices.orderservice.component

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.Stores
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import springmicroservices.orderservice.helpers.OrderDeserializer
import springmicroservices.orderservice.helpers.OrderSerializer
import springmicroservices.orderservice.models.OrderModel
import springmicroservices.orderservice.service.OrderService
import java.time.Duration
import java.util.Properties
import java.util.concurrent.Executor


@Component
class Consumer(
    @Value("\${spring.kafka.bootstrapServers}")
    private val servers: String,
    private val orderService: OrderService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

//    @KafkaListener(topics = ["account-order", "account-order"], groupId = "order")
//    fun listenStore(order: OrderModel) {
//        val o = orderService.confirm(order, order)
//        orderService.send(o)
//        logger.info("Receive confirmation from account {}: {}", order.source, order)
//    }
}