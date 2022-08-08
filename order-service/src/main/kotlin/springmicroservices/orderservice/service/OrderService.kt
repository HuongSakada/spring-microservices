package springmicroservices.orderservice.service

import org.springframework.stereotype.Service
import springmicroservices.orderservice.helpers.enum.Order.StatusEnum
import springmicroservices.orderservice.models.OrderModel

@Service
class OrderService {
    fun confirm(orderStore: OrderModel): OrderModel {
        val order = OrderModel(
            orderStore.id,
            orderStore.customerId,
            orderStore.productId,
            orderStore.productCount,
            orderStore.price
        )

        if (orderStore.status == StatusEnum.ACCEPT.id) {
            order.status = StatusEnum.CONFIRMED.id
        } else if (orderStore.status == StatusEnum.REJECT.id) {
            order.status = StatusEnum.REJECTED.id
        }

        return order;
    }
}