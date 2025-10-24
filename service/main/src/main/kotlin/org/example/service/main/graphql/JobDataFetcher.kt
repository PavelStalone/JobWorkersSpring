package org.example.service.main.graphql

import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment
import org.example.api.dto.JobChangeRequest
import org.example.api.dto.JobRequest
import org.example.api.dto.JobResponse
import org.example.service.main.service.JobService

@DgsComponent
class JobDataFetcher(
    private val jobService: JobService
) {

    @DgsQuery
    fun jobById(@InputArgument id: Long) = jobService.getJobById(id)

    @DgsQuery
    fun jobs(@InputArgument page: Int, @InputArgument size: Int) = jobService.getAllJobs(page, size)

    @DgsData(parentType = "Job", field = "worker")
    fun worker(dfe: DataFetchingEnvironment) = dfe.getSource<JobResponse>()!!.worker

    @DgsMutation
    fun createJob(@InputArgument("input") input: Map<String, Any?>) = jobService.createJob(
        jobRequest = JobRequest(
            workerId = input.getValue("workerId").toString().toLong(),
            cost = input.getValue("cost").toString().toInt(),
            time = input.getValue("time").toString().toLong(),
            category = input.getValue("category").toString(),
            description = input.getValue("description").toString()
        )
    )

    @DgsMutation
    fun updateJob(
        @InputArgument id: Long,
        @InputArgument("input") input: Map<String, Any?>
    ) = jobService.updateJob(
        id = id,
        jobChangeRequest = JobChangeRequest(
            workerId = input["workerId"]?.toString()?.toLong(),
            status = input["workerId"]?.toString(),
            finishRate = input["workerId"]?.toString()?.toFloat()
        )
    )

    @DgsMutation
    fun deleteJob(@InputArgument id: Long): Long {
        jobService.deleteJob(id)

        return id
    }
}
