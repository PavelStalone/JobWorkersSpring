package org.example.service.analytic

import io.grpc.stub.StreamObserver
import org.example.grpc.AnalyticsServiceGrpc
import org.example.grpc.WorkerRatingRequest
import org.example.grpc.WorkerRatingResponse
import org.springframework.grpc.server.service.GrpcService
import java.lang.Math.random

@GrpcService
class AnalyticService : AnalyticsServiceGrpc.AnalyticsServiceImplBase() {

    override fun calculateWorkerRating(
        request: WorkerRatingRequest,
        responseObserver: StreamObserver<WorkerRatingResponse>
    ) {
        val score = (random() * 100).toInt()
        val verdict = "GOOD".takeIf { score > 50 } ?: "BAD"

        val response = WorkerRatingResponse.newBuilder()
            .setWorkerId(request.workerId)
            .setRatingScore(score)
            .setVerdict(verdict)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
