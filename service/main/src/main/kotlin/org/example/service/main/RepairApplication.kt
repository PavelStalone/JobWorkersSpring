package org.example.service.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.grpc.client.ImportGrpcClients
import org.springframework.hateoas.config.EnableHypermediaSupport

@ImportGrpcClients(basePackages = ["org.example.grpc"])
@SpringBootApplication(
    scanBasePackages = ["org.example.service.main", "org.example.api", "org.example.event", "org.example.grpc"],
    exclude = [DataSourceAutoConfiguration::class]
)
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class RepairApplication

fun main(args: Array<String>) {
    runApplication<RepairApplication>(*args)
}
