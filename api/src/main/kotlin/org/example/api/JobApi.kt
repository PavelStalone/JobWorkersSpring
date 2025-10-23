package org.example.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.api.dto.JobChangeRequest
import org.example.api.dto.JobRequest
import org.example.api.dto.JobResponse
import org.example.api.dto.StatusResponse
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "jobs", description = "API for work with jobs")
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
@RequestMapping("/api/jobs")
interface JobApi {

    @Operation(summary = "Create new job")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(responseCode = "201", description = "Job successful created")
    fun createJob(@Valid @RequestBody jobRequest: JobRequest): ResponseEntity<EntityModel<JobResponse>>

    @Operation(summary = "Find job by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Job found")
    fun getJobById(@PathVariable id: Long): EntityModel<JobResponse>

    @Operation(summary = "Update job")
    @ApiResponse(responseCode = "200", description = "Job successful updated")
    @PostMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateJob(
        @PathVariable id: Long,
        @Valid @RequestBody jobChangeRequest: JobChangeRequest
    ): EntityModel<JobResponse>

    @Operation(summary = "Get all jobs")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Jobs found")
    fun getAllJobs(
        @Parameter(description = "Page number (0..N)")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Page size")
        @RequestParam(defaultValue = "10")
        size: Int,
    ): PagedModel<EntityModel<JobResponse>>

    @Operation(summary = "Delete job")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Job removed")
    @ApiResponse(responseCode = "404", description = "Job not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteJob(@PathVariable id: Long)
}
