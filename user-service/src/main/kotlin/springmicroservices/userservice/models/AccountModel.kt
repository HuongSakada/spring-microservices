package springmicroservices.userservice.models

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "accounts")
data class AccountModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAccounts")
    @SequenceGenerator(name = "seqAccounts", sequenceName = "SEQ_ACCOUNTS", allocationSize = 1)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String,

    @Column(name = "amount_available")
    var amountAvailable: Double,

    @Column(name = "amount_reserved")
    var amountReserved: Double
) {
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    @JsonBackReference
//    lateinit var user: UserModel
}