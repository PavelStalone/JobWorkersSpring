package org.example.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

data class WorkerRequest(
    @NotBlank val name: String,
    @NotBlank val description: String,
)

data class WorkerChangeRequest(
    @NotBlank val name: String?,
    @NotBlank val description: String?,
)

@Relation(collectionRelation = "workers", itemRelation = "worker")
data class WorkerResponse(
    val id: Long,
    val name: String,
    val description: String,
): RepresentationModel<WorkerResponse>()
