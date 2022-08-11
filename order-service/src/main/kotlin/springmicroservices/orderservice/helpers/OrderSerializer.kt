package springmicroservices.orderservice.helpers

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory
import springmicroservices.orderservice.models.OrderModel

class OrderSerializer : Serializer<OrderModel> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun close() {}
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
    override fun serialize(topic: String, data: OrderModel?): ByteArray? {
        log.info("Serializing order...")
        return jsonMapper.writeValueAsBytes(
            data ?: throw SerializationException("Error during serializing Order to ByteArray[]")
        )
    }
}