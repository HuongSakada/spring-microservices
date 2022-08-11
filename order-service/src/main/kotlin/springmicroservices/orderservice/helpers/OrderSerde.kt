package springmicroservices.orderservice.helpers

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import springmicroservices.orderservice.models.OrderModel

class OrderSerde : Serde<OrderModel> {
    override fun close() {}
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
    override fun serializer(): Serializer<OrderModel> = OrderSerializer()
    override fun deserializer(): Deserializer<OrderModel> = OrderDeserializer()
}