package springmicroservices.orderservice.models

import javax.persistence.*

data class OrderModel(
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrders")
//    @SequenceGenerator(name = "seqOrders", sequenceName = "SEQ_ORDERS", allocationSize = 1)
//    @Column(name = "id")
    val id: Long = 0,

    val customerId: Long,
    val productId: Long,
    val productCount: Int,
    val price: Double,
    val status: String,
    val source: String,
) {}
