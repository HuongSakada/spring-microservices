package springmicroservices.orderservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.bootstrapAddress}")
    private val servers: String,

    @Value("\${spring.kafka.topics.order}")
    private val topic: String
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        return  KafkaAdmin(configs)
    }

    fun orderTopic(): NewTopic {
        return NewTopic(topic, 1, 1.toShort())
    }
}