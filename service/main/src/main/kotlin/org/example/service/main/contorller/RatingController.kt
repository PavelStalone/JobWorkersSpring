package org.example.service.main.contorller

import net.devh.boot.grpc.client.inject.GrpcClient
import org.example.api.RatingApi
import org.example.event.RabbitMQ
import org.example.event.WorkerEvent
import org.example.grpc.AnalyticsServiceGrpc
import org.example.grpc.WorkerRatingRequest
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
class RatingController(
    @GrpcClient("analytics-service") private val analyticStub: AnalyticsServiceGrpc.AnalyticsServiceBlockingStub,
    private val rabbitTemplate: RabbitTemplate,
) : RatingApi {

    override fun rateWorker(id: Long): String {
        val request = WorkerRatingRequest.newBuilder()
            .setWorkerId(id)
            .build()

        val grpcResponse = analyticStub.calculateWorkerRating(request)

        val event = WorkerEvent.WorkerRatedEvent(
            workerId = grpcResponse.workerId,
            score = grpcResponse.ratingScore,
            verdict = grpcResponse.verdict,
        )

        rabbitTemplate.convertAndSend(RabbitMQ.FANOUT_EXCHANGE_NAME, "", event)

        return "Rating calculated: ${grpcResponse.ratingScore}"
    }
}
