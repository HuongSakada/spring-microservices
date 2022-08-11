package springmicroservices.orderservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.kstream.Joined
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.kstream.StreamJoined
import org.apache.kafka.streams.state.Stores
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.scheduling.annotation.EnableAsync
import springmicroservices.orderservice.helpers.OrderSerde
import springmicroservices.orderservice.models.OrderModel
import springmicroservices.orderservice.service.OrderService
import java.time.Duration

@Configuration
@EnableKafka
@EnableKafkaStreams
@EnableAsync
class KafkaConfig(
    @Value("\${spring.kafka.bootstrapServers}")
    private val servers: String,
    private val orderService: OrderService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        configs[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return  KafkaAdmin(configs)
    }

    @Bean
    fun orderTopic(): NewTopic {
        return TopicBuilder
            .name(ORDER_TOPIC)
            .partitions(1)
            .build()
    }

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun defaultKafkaStreamsConfig(): KafkaStreamsConfiguration {
        val props: MutableMap<String, Any> = HashMap()
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        props[StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG] = LogAndContinueExceptionHandler::class.java
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "order-stream"
        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.Long().javaClass.name
        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = OrderSerde::class.java
        props[ConsumerConfig.GROUP_ID_CONFIG] = "order-stream-group"
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return KafkaStreamsConfiguration(props)
    }

    @Bean
    fun stream(builder: StreamsBuilder): KStream<Long, OrderModel> {
        val longSerde = Serdes.Long()
        val orderSerde = JsonSerde<OrderModel>()
        val productStream: KStream<Long, OrderModel> = builder.stream("product-order")
        val accountStream: KStream<Long, OrderModel> = builder.stream("account-order")

        productStream
            .join(
                accountStream,
                orderService::confirm,
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(10)),
                StreamJoined.with(longSerde, orderSerde, orderSerde)
            )
            .peek { _, value -> logger.info("Order stream output: {}", value) }
            .to("order", Produced.with(longSerde, orderSerde))

        return productStream
    }

//    @Bean
//    fun table(builder: StreamsBuilder): KTable<Long, OrderModel> {
//        val account = Stores.persistentKeyValueStore("order")
//        val orderSerde = JsonSerde<OrderModel>()
//        val stream: KStream<Long, OrderModel> = builder.stream("order", Consumed.with(Serdes.Long(), orderSerde))
//
//        return stream.toTable(Materialized.`as`<Long?, OrderModel?>(account)
//            .withKeySerde(Serdes.Long())
//            .withValueSerde(orderSerde)
//        )
//    }
}

// CONSTANT TOPICS
const val ORDER_TOPIC = "order"