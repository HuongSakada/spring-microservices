package springmicroservices.userservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@EnableKafka
class KafkaConfig(
    @Value("\${spring.kafka.bootstrapServers}")
    private val servers: String
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        return  KafkaAdmin(configs)
    }

    @Bean
    fun orderTopic(): NewTopic {
        return TopicBuilder
            .name(ACCOUNT_TOPIC)
            .partitions(1)
            .build()
    }
}

// CONSTANT TOPICS
const val ACCOUNT_TOPIC = "account-order"