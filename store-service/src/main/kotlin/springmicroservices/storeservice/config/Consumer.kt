package springmicroservices.storeservice.config

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class Consumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["order"], groupId = "order_group")
    fun listenGroupLog(consumerRecord: ConsumerRecord<Any, Any>) {
        logger.info("Message received {}", consumerRecord)
        println("Message received $consumerRecord")
    }
}