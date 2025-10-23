package org.example.api

import org.example.api.dto.StatusResponse
import org.example.api.dto.WorkerRequest
import org.example.api.dto.WorkerResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.api.dto.WorkerChangeRequest
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "workers", description = "API for work with workers")
@ApiResponse(
    responseCode = "400",
    description = "Error validation",
    content = [Content(mediaType = "application/json", schema = Schema(implementation = StatusResponse::class))]
)
@ApiResponse(
    responseCode = "500",
    description = "Internal error",
    content = [Content(mediaType = "application/json", schema = Schema(implementation = StatusResponse::class))]
)
@RequestMapping("api/workers")
interface WorkerApi {

    @Operation(summary = "Create new worker")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(responseCode = "201", description = "Worker successful created")
    fun createWorker(@Valid @RequestBody workerRequest: WorkerRequest): ResponseEntity<EntityModel<WorkerResponse>>

    @Operation(summary = "Update worker")
    @PostMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(responseCode = "200", description = "Worker successful updated")
    fun updateWorker(@PathVariable id: Long, @Valid @RequestBody workerChangeRequest: WorkerChangeRequest): EntityModel<WorkerResponse>

    @GetMapping("/{id}")
    @Operation(summary = "Find worker by id")
    @ApiResponse(responseCode = "200", description = "Worker found")
    fun getWorkerById(@PathVariable id: Long): EntityModel<WorkerResponse>

    @Operation(summary = "Get all workers")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Workers found")
    fun getAllWorkers(
        @Parameter(description = "Page number (0..N)")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Page size")
        @RequestParam(defaultValue = "10")
        size: Int,
    ): PagedModel<EntityModel<WorkerResponse>>

    @Operation(summary = "Delete worker")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Worker removed")
    @ApiResponse(responseCode = "404", description = "Worker not found")
    fun deleteWorker(@PathVariable id: Long)
}
