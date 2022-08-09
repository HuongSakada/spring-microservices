package springmicroservices.userservice.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import springmicroservices.userservice.models.AccountModel

@Repository
interface AccountRepository: CrudRepository<AccountModel, Long> {}