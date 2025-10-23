package org.example.main.service

import org.example.api.dto.JobChangeRequest
import org.example.api.dto.JobRequest
import org.example.api.dto.JobResponse
import org.example.api.dto.PagedResponse
import org.springframework.stereotype.Service
import kotlin.math.ceil
import kotlin.math.min

interface JobService {

    fun createJob(jobRequest: JobRequest): JobResponse
    fun getJobById(id: Long): JobResponse
    fun updateJob(id: Long, jobChangeRequest: JobChangeRequest): JobResponse
    fun getAllJobs(page: Int, size: Int): PagedResponse<JobResponse>
    fun deleteJob(id: Long)
}

@Service
class JobServiceInMemory(
    private val workerService: WorkerService,
) : JobService {

    private val data: MutableMap<Long, JobResponse> = mutableMapOf()
    private var id = 0L

    override fun createJob(jobRequest: JobRequest): JobResponse = JobResponse(
        id = id++,
        cost = jobRequest.cost,
        status = "CREATED",
        workTime = jobRequest.time,
        category = jobRequest.category,
        finishRate = 0f,
        description = jobRequest.description,
        worker = workerService.getWorkerById(jobRequest.workerId),
        endTimeStamp = null,
        startTimeStamp = null,
    ).also { job ->
        data[job.id] = job
    }

    override fun getJobById(id: Long): JobResponse = data.getValue(id)

    override fun updateJob(
        id: Long,
        jobChangeRequest: JobChangeRequest
    ): JobResponse = requireNotNull(
        data.computeIfPresent(id) { _, job ->
            job.copy(
                status = jobChangeRequest.status ?: job.status,
                finishRate = jobChangeRequest.finishRate ?: job.finishRate,
                worker = jobChangeRequest.workerId?.let { workerService.getWorkerById(it) } ?: job.worker,
            )
        }
    )

    override fun getAllJobs(
        page: Int,
        size: Int
    ): PagedResponse<JobResponse> {
        val jobs = data.values
            .sortedBy(JobResponse::id)
            .toList()

        val totalElements = jobs.size
        val totalPages = ceil(totalElements / size.toDouble()).toInt()
        val fromIndex = page * size
        val toIndex = min(fromIndex + size, totalElements)

        return PagedResponse(
            content = emptyList<JobResponse>().takeIf { fromIndex > toIndex } ?: jobs.subList(fromIndex, toIndex),
            pageNumber = page,
            pageSize = size,
            totalElements = totalElements.toLong(),
            totalPages = totalPages,
            last = page >= totalPages - 1,
        )
    }

    override fun deleteJob(id: Long) {
        data.remove(id)
    }
}
