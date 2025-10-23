package org.example.main.service

import org.example.api.dto.PagedResponse
import org.example.api.dto.WorkerChangeRequest
import org.example.api.dto.WorkerRequest
import org.example.api.dto.WorkerResponse
import org.springframework.stereotype.Service
import kotlin.math.ceil
import kotlin.math.min

interface WorkerService {

    fun createWorker(workerRequest: WorkerRequest): WorkerResponse
    fun getWorkerById(id: Long): WorkerResponse
    fun updateWorker(id: Long, workerChangeRequest: WorkerChangeRequest): WorkerResponse
    fun getAllWorkers(page: Int, size: Int): PagedResponse<WorkerResponse>
    fun deleteWorker(id: Long)
}

@Service
class WorkServiceInMemory : WorkerService {

    private val data: MutableMap<Long, WorkerResponse> = mutableMapOf()
    private var id = 0L

    override fun createWorker(workerRequest: WorkerRequest): WorkerResponse = WorkerResponse(
        id = id++,
        name = workerRequest.name,
        description = workerRequest.description,
    ).also { worker ->
        data[worker.id] = worker
    }

    override fun getWorkerById(id: Long): WorkerResponse = data.getValue(id)

    override fun updateWorker(
        id: Long,
        workerChangeRequest: WorkerChangeRequest
    ): WorkerResponse = requireNotNull(
        data.computeIfPresent(id) { _, worker ->
            worker.copy(
                name = workerChangeRequest.name ?: worker.name,
                description = workerChangeRequest.description ?: worker.description,
            )
        }
    )

    override fun getAllWorkers(
        page: Int,
        size: Int
    ): PagedResponse<WorkerResponse> {
        val workers = data.values
            .sortedBy(WorkerResponse::id)
            .toList()

        val totalElements = workers.size
        val totalPages = ceil(totalElements / size.toDouble()).toInt()
        val fromIndex = page * size
        val toIndex = min(fromIndex + size, totalElements)

        return PagedResponse(
            content = emptyList<WorkerResponse>().takeIf { fromIndex > toIndex } ?: workers.subList(fromIndex, toIndex),
            pageNumber = page,
            pageSize = size,
            totalElements = totalElements.toLong(),
            totalPages = totalPages,
            last = page >= totalPages - 1,
        )
    }

    override fun deleteWorker(id: Long) {
        data.remove(id)
    }
}
