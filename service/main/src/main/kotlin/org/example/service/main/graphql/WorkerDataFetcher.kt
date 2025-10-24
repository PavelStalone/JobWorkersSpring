package org.example.service.main.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.example.api.dto.WorkerChangeRequest
import org.example.api.dto.WorkerRequest
import org.example.service.main.service.WorkerService

@DgsComponent
class WorkerDataFetcher(
    private val workerService: WorkerService
) {

    @DgsQuery
    fun workerById(@InputArgument id: Long) = workerService.getWorkerById(id)

    @DgsQuery
    fun workers(@InputArgument page: Int, @InputArgument size: Int) = workerService.getAllWorkers(page, size)

    @DgsMutation
    fun createWorker(@InputArgument("input") input: Map<String, Any?>) = workerService.createWorker(
        workerRequest = WorkerRequest(
            name = input.getValue("name").toString(),
            description = input.getValue("description").toString()
        )
    )

    @DgsMutation
    fun updateWorker(
        @InputArgument id: Long,
        @InputArgument("input") input: Map<String, Any?>
    ) = workerService.updateWorker(
        id = id,
        workerChangeRequest = WorkerChangeRequest(
            name = input["name"]?.toString(),
            description = input["description"]?.toString(),
        )
    )

    @DgsMutation
    fun deleteWorker(@InputArgument id: Long): Long {
        workerService.deleteWorker(id)

        return id
    }
}
