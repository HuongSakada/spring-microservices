package springmicroservices.storeservice.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "products")
data class ProductModel (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProducts")
    @SequenceGenerator(name = "seqProducts", sequenceName = "SEQ_PRODUCTS", allocationSize = 1)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String,

    @Column(name = "available_items")
    var availableItems: Int? = 0,

    @Column(name = "reserved_items")
    var reservedItems: Int? = 0,
){
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    lateinit var store: StoreModel
}