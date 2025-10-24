package org.example.service.main.contorller

import org.example.api.WorkerApi
import org.example.api.dto.WorkerChangeRequest
import org.example.api.dto.WorkerRequest
import org.example.api.dto.WorkerResponse
import org.example.service.main.assembler.WorkerModelAssembler
import org.example.service.main.service.WorkerService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class WorkerController(
    private val workerService: WorkerService,
    private val workerModelAssembler: WorkerModelAssembler,
    private val pagedResourceAssembler: PagedResourcesAssembler<WorkerResponse>
) : WorkerApi {

    override fun createWorker(workerRequest: WorkerRequest): ResponseEntity<EntityModel<WorkerResponse>> {
        val worker = workerService.createWorker(workerRequest = workerRequest)
        val entityModel = workerModelAssembler.toModel(worker)

        return ResponseEntity
            .created(entityModel.getRequiredLink("self").toUri())
            .body(entityModel)
    }

    override fun updateWorker(
        id: Long,
        workerChangeRequest: WorkerChangeRequest
    ): EntityModel<WorkerResponse> {
        val worker = workerService.updateWorker(id, workerChangeRequest)

        return workerModelAssembler.toModel(worker)
    }

    override fun getWorkerById(id: Long): EntityModel<WorkerResponse> {
        val worker = workerService.getWorkerById(id = id)

        return workerModelAssembler.toModel(worker)
    }

    override fun getAllWorkers(
        page: Int,
        size: Int
    ): PagedModel<EntityModel<WorkerResponse>> {
        val workerPage = with(workerService.getAllWorkers(page, size)) {
            PageImpl(
                content,
                PageRequest.of(pageNumber, pageSize),
                totalElements
            )
        }

        return pagedResourceAssembler.toModel(workerPage, workerModelAssembler)
    }

    override fun deleteWorker(id: Long) {
        workerService.deleteWorker(id)
    }
}
