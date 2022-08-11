package springmicroservices.orderservice.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import springmicroservices.orderservice.config.ORDER_TOPIC
import springmicroservices.orderservice.helpers.enum.Order.SourceEnum
import springmicroservices.orderservice.helpers.enum.Order.StatusEnum
import springmicroservices.orderservice.models.OrderModel

@Service
class OrderService (
    private val orderTemplate: KafkaTemplate<Long, OrderModel>
) {
    fun send(order: OrderModel) {
        orderTemplate.send(ORDER_TOPIC, order.id, order)
    }

    fun confirm(productOrder: OrderModel, customerOrder: OrderModel): OrderModel {
        val order = OrderModel(
            productOrder.id,
            productOrder.customerId,
            productOrder.productId,
            productOrder.productCount,
            productOrder.price
        )

        if (productOrder.status == StatusEnum.ACCEPT.id && customerOrder.status == StatusEnum.ACCEPT.id) {
            order.status = StatusEnum.CONFIRMED.id
        } else if (productOrder.status == StatusEnum.REJECT.id && customerOrder.status == StatusEnum.REJECT.id) {
            order.status = StatusEnum.REJECTED.id
        } else if (productOrder.status == StatusEnum.REJECT.id || customerOrder.status == StatusEnum.REJECT.id) {
            order.status = StatusEnum.ROLLBACK.id
            order.source = if (productOrder.source == SourceEnum.PRODUCT.toString()) SourceEnum.PRODUCT.toString() else SourceEnum.ACCOUNT.toString()
        }

        return order;
    }
}