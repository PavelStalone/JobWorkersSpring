package org.example.service.audit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.example.service.audit"])
class AuditApplication

fun main(args: Array<String>) {
    runApplication<AuditApplication>(*args)
}
