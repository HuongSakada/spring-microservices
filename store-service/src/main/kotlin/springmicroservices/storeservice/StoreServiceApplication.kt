package springmicroservices.storeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
class StoreServiceApplication

fun main(args: Array<String>) {
	runApplication<StoreServiceApplication>(*args)
}
