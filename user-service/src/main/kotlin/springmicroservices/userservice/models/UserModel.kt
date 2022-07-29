package springmicroservices.userservice.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserModel (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUsers")
    @SequenceGenerator(name = "seqUsers", sequenceName = "SEQ_USERS", allocationSize = 1)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String,

    @Column(name = "email")
    var email: String,
){
    @OneToOne(targetEntity = AccountModel::class, mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonManagedReference
    var account: AccountModel? = null
}