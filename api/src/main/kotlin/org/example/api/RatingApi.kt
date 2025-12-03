package org.example.api

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("api/workers")
interface RatingApi {

    @PostMapping("/{id}/rate")
    fun rateWorker(@PathVariable id: Long): String
}
