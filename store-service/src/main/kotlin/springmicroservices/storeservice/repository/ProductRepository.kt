package springmicroservices.storeservice.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import springmicroservices.storeservice.models.ProductModel

@Repository
interface ProductRepository: CrudRepository<ProductModel, Long> {}