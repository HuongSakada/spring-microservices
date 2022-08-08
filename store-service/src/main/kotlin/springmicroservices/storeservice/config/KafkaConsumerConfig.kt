package springmicroservices.storeservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.JsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import springmicroservices.storeservice.models.OrderModel

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrapServers}")
    private val servers: String
) {
    @Bean
    fun orderConsumerFactory(): ConsumerFactory<Long, OrderModel> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun converter(): RecordMessageConverter? {
        return JsonMessageConverter()
    }
}