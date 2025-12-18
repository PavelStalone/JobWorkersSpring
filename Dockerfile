FROM gradle:8.13.0-jdk17-corretto AS builder
WORKDIR /app

COPY gradlew gradlew
COPY gradle/ gradle/
COPY settings.gradle.kts ./

COPY api/src ./api/src
COPY api/build.gradle.kts ./api/build.gradle.kts

COPY grpc/src ./grpc/src
COPY grpc/build.gradle.kts ./grpc/build.gradle.kts

COPY event/src ./event/src
COPY event/build.gradle.kts ./event/build.gradle.kts

RUN ./gradlew build --no-daemon --dry-run

COPY service ./service
RUN ./gradlew clean build --no-daemon --no-build-cache


FROM busybox
COPY --from=builder /app/service/audit/build/libs/*.jar /artifact/audit/app.jar
COPY --from=builder /app/service/main/build/libs/*.jar /artifact/main/app.jar
COPY --from=builder /app/service/notification/build/libs/*.jar /artifact/notification/app.jar
COPY --from=builder /app/service/analytic/build/libs/*.jar /artifact/analytic/app.jar
ENTRYPOINT echo "Build done"
