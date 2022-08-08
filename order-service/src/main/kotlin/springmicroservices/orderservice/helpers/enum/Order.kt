package springmicroservices.orderservice.helpers.enum

object Order {
    enum class StatusEnum(val id: Long) {
        OPEN(1),
        ACCEPT(2),
        REJECT(3),
        CONFIRMED(4),
        REJECTED(5),
        ROLLBACK(6);

        companion object {
            fun ids(): LongArray = values().map { it.id }.toLongArray()
        }
    }

    enum class SourceEnum(val id: Long) {
        ORDER(1),
        PRODUCT(2),
        STORE(3),
        USER(4),
        ACCOUNT(5);

        companion object {
            fun ids(): LongArray = values().map { it.id }.toLongArray()
        }
    }
}
