package org.example.event

sealed interface JobEvent : Event {

    data class JobCreatedEvent(
        val jobId: Long,
        val category: String,
        val description: String,
    ) : JobEvent

    data class JobDeletedEvent(
        val jobId: Long,
    ) : JobEvent
}
