package org.example.service.main.assembler

import org.example.api.dto.JobResponse
import org.example.service.main.contorller.JobController
import org.example.service.main.contorller.WorkerController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

@Component
class JobModelAssembler : RepresentationModelAssembler<JobResponse, EntityModel<JobResponse>> {

    override fun toModel(job: JobResponse): EntityModel<JobResponse> {
        return EntityModel.of(
            job,
            linkTo(methodOn(JobController::class.java).getJobById(job.id)).withSelfRel(),
            linkTo(methodOn(WorkerController::class.java).getAllWorkers(0, 10)).withRel("workers"),
            linkTo(methodOn(JobController::class.java).getAllJobs(0, 10)).withRel("collection"),
        )
    }
}
