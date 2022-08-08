package springmicroservices.storeservice.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import springmicroservices.storeservice.models.StoreModel

@Repository
interface StoreRepository : CrudRepository<StoreModel, Long>{}