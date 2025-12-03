plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "app"

include(":service:main")
include(":api")
include(":event")
include(":service:audit")
include(":grpc")
include(":service:analytic")