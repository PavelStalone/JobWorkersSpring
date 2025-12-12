FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY /build/libs/*.jar app.jar

RUN chown javauser:javauser /app/app.jar
USER javauser

# Порт сервиса
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]