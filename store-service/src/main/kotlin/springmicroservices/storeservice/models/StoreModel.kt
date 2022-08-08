package springmicroservices.storeservice.models

import javax.persistence.*

@Entity
@Table(name = "stores")
data class StoreModel (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqShops")
    @SequenceGenerator(name = "seqShops", sequenceName = "SEQ_SHOPS", allocationSize = 1)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String,

    @Column(name = "address")
    var address: String? = null
){
//    @OneToMany(targetEntity = ProductModel::class, mappedBy = "store", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
//    var products: Set<ProductModel> = setOf()
}