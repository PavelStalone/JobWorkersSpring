# Используем легковесный образ JRE 21 на базе Alpine Linux
FROM eclipse-temurin:21-jre-alpine

# Создаем рабочую директорию
WORKDIR /app

# Создаем пользователя 'javauser', чтобы не запускать приложение от root (Security Best Practice)
RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY /build/libs/*.jar app.jar

# Меняем владельца файла
RUN chown javauser:javauser /app/app.jar

# Переключаемся на пользователя
USER javauser

# Документируем порт (этот порт должен совпадать с server.port в properties)
EXPOSE 8080

# Запускаем приложение
# Использование exec формы ["..."] позволяет корректно передавать сигналы остановки (SIGTERM)
ENTRYPOINT ["java", "-jar", "app.jar"]