# Этап 1: Сборка (используем официальный образ с уже установленным Gradle и JDK 21)
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# Копируем весь проект из корня (благодаря context: . в docker-compose)
COPY . .

# Собираем проект, используя системную команду 'gradle', а не './gradlew'.
# Поскольку Gradle уже предустановлен в этом образе, он НЕ будет пытаться
# скачать ничего из интернета, что полностью решает проблему UnknownHostException.
RUN gradle :user-service:build -x test --no-daemon

# Этап 2: Запуск (легкий образ, содержащий только JRE для экономии места)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Копируем собранный JAR-файл из этапа сборки
COPY --from=builder /app/user-service/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]