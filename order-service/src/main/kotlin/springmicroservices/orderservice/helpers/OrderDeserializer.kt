package springmicroservices.orderservice.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import springmicroservices.orderservice.models.OrderModel

class OrderDeserializer : Deserializer<OrderModel> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String, data: ByteArray?): OrderModel {
        log.info("Deserializing order...")
        if (data == null) throw SerializationException("Error during deserializing ByteArray[] to Order")
        return objectMapper.readValue(data, OrderModel::class.java)
    }

    override fun close() {}
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
}