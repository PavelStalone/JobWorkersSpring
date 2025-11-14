package org.example.event

object RabbitMQ {

    const val EXCHANGE_NAME = "repair-exchange"
    const val DL_EXCHANGE_NAME = "dl-exchange"

    const val ALL_WILDCARD = "#"
    const val ANY_WILDCARD = "*"

    object Notification {

        const val dead_name = "dlq.notification"
        const val name = "notification"

        object Job {

            const val name = Notification.name + ".job"

            object Created {

                const val name = Job.name + ".created"
            }

            object Deleted {

                const val name = Job.name + ".deleted"
            }
        }

        object Worker {

            const val name = Notification.name + ".worker"

            object Created {

                const val name = Worker.name + ".created"
            }

            object Deleted {

                const val name = Worker.name + ".deleted"
            }
        }
    }
}
