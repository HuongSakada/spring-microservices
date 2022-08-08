package springmicroservices.storeservice.repository

import org.springframework.data.repository.CrudRepository
import springmicroservices.storeservice.models.ProductModel

interface ProductRepository : CrudRepository<ProductModel, Long> {}