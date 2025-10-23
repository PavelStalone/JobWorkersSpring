package org.example.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport

@SpringBootApplication(
    scanBasePackages = ["org.example.main", "org.example.api"],
    exclude = [DataSourceAutoConfiguration::class]
)
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class RepairApplication

fun main(args: Array<String>) {
    runApplication<RepairApplication>(*args)
}
