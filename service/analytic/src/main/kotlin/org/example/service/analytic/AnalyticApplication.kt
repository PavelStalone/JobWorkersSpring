package org.example.service.analytic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.example.service.analytic"])
class AnalyticApplication

fun main(args: Array<String>) {
    runApplication<AnalyticApplication>(*args)
}
