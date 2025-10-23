package org.example.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

data class JobRequest(
    val workerId: Long,
    @Min(0) val cost: Int,
    @Min(1) val time: Long,
    @NotBlank val category: String,
    @NotBlank val description: String,
)

data class JobChangeRequest(
    val workerId: Long?,
    @NotBlank val status: String?,
    @Size(min = 0, max = 1) val finishRate: Float?,
)

@Relation(collectionRelation = "jobs", itemRelation = "job")
data class JobResponse(
    val id: Long,
    val cost: Int,
    val status: String,
    val workTime: Long,
    val category: String,
    val finishRate: Float,
    val description: String,
    val worker: WorkerResponse,
    val endTimeStamp: LocalDateTime?,
    val startTimeStamp: LocalDateTime?,
) : RepresentationModel<JobResponse>()
