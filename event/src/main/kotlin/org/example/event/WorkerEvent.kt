package org.example.event

sealed interface WorkerEvent : Event {

    data class WorkerCreatedEvent(
        val workerId: Long,
        val name: String,
        val description: String,
    ) : WorkerEvent

    data class WorkerDeletedEvent(
        val workerId: Long,
    ) : WorkerEvent

    data class WorkerRatedEvent(
        val workerId: Long,
        val score: Int,
        val verdict: String,
    ): WorkerEvent
}
