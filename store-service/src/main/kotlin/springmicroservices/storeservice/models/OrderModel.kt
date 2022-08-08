package springmicroservices.storeservice.models

data class OrderModel(
    var id: Long,
    val customerId: Long,
    val productId: Long,
    val productCount: Int,
    val price: Double,
    var status: Long? = 0,
    var source: String? = "",
) {}