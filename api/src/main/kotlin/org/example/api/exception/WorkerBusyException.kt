package org.example.api.exception

class WorkerBusyException(id: Any) : RuntimeException("Worker with $id ID already work")
