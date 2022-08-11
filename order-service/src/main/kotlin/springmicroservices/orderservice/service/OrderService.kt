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

    fun confirm(orderStore: OrderModel, orderCustomer: OrderModel): OrderModel {
        val order = OrderModel(
            orderStore.id,
            orderStore.customerId,
            orderStore.productId,
            orderStore.productCount,
            orderStore.price
        )

        if (orderStore.status == StatusEnum.ACCEPT.id && orderCustomer.status == StatusEnum.ACCEPT.id) {
            order.status = StatusEnum.CONFIRMED.id
        } else if (orderStore.status == StatusEnum.REJECT.id && orderCustomer.status == StatusEnum.REJECT.id) {
            order.status = StatusEnum.REJECTED.id
        } else if (orderStore.status == StatusEnum.REJECT.id || orderCustomer.status == StatusEnum.REJECT.id) {
            order.status = StatusEnum.ROLLBACK.id
            order.source = if (orderStore.source == SourceEnum.PRODUCT.toString()) SourceEnum.PRODUCT.toString() else SourceEnum.ACCOUNT.toString()
        }

        return order;
    }
}