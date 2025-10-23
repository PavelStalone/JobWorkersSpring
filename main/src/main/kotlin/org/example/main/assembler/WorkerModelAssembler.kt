package org.example.main.assembler

import org.example.api.dto.WorkerResponse
import org.example.main.contorller.JobController
import org.example.main.contorller.WorkerController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

@Component
class WorkerModelAssembler : RepresentationModelAssembler<WorkerResponse, EntityModel<WorkerResponse>> {

    override fun toModel(worker: WorkerResponse): EntityModel<WorkerResponse> {
        return EntityModel.of(
            worker,
            linkTo(methodOn(WorkerController::class.java).getWorkerById(worker.id)).withSelfRel(),
            linkTo(methodOn(JobController::class.java).getAllJobs(0, 10)).withRel("jobs"),
            linkTo(methodOn(WorkerController::class.java).getAllWorkers(0, 10)).withRel("collection"),
        )
    }
}
