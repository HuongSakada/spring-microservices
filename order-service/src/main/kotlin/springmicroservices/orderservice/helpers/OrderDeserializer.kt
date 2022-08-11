package springmicroservices.orderservice.helpers

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import springmicroservices.orderservice.models.OrderModel

class OrderDeserializer : Deserializer<OrderModel> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun close() {}
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
    override fun deserialize(topic: String, data: ByteArray?): OrderModel {
        log.info("Deserializing order...")
        if (data == null) throw SerializationException("Error during deserializing ByteArray[] to Order")
        return jsonMapper.readValue(data, OrderModel::class.java)
    }
}