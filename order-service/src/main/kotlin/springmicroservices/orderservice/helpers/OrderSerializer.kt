package springmicroservices.orderservice.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory
import springmicroservices.orderservice.models.OrderModel

class OrderSerializer : Serializer<OrderModel> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun serialize(topic: String?, data: OrderModel?): ByteArray? {
        log.info("Serializing order...")
        return objectMapper.writeValueAsBytes(
            data ?: throw SerializationException("Error during serializing Order to ByteArray[]")
        )
    }

    override fun close() {}
}