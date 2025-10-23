package org.example.main.contorller

import org.example.api.JobApi
import org.example.api.dto.JobChangeRequest
import org.example.api.dto.JobRequest
import org.example.api.dto.JobResponse
import org.example.main.assembler.JobModelAssembler
import org.example.main.service.JobService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class JobController(
    private val jobService: JobService,
    private val jobModelAssembler: JobModelAssembler,
    private val pagedResourceAssembler: PagedResourcesAssembler<JobResponse>
) : JobApi {

    override fun createJob(jobRequest: JobRequest): ResponseEntity<EntityModel<JobResponse>> {
        val job = jobService.createJob(jobRequest)
        val entityModel = jobModelAssembler.toModel(job)

        return ResponseEntity
            .created(entityModel.getRequiredLink("self").toUri())
            .body(entityModel)
    }

    override fun getJobById(id: Long): EntityModel<JobResponse> {
        val job = jobService.getJobById(id)

        return jobModelAssembler.toModel(job)
    }

    override fun updateJob(
        id: Long,
        jobChangeRequest: JobChangeRequest
    ): EntityModel<JobResponse> {
        val job = jobService.updateJob(id, jobChangeRequest)

        return jobModelAssembler.toModel(job)
    }

    override fun getAllJobs(
        page: Int,
        size: Int
    ): PagedModel<EntityModel<JobResponse>> {
        val jobPage = with(jobService.getAllJobs(page, size)) {
            PageImpl(
                content,
                PageRequest.of(pageNumber, pageSize),
                totalElements
            )
        }

        return pagedResourceAssembler.toModel(jobPage, jobModelAssembler)
    }

    override fun deleteJob(id: Long) {
        jobService.deleteJob(id)
    }
}
