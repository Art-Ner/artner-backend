FROM gradle:8.10-jdk21 AS builder
WORKDIR /workspace

COPY settings.gradle* build.gradle* gradle.* gradle/ ./
RUN gradle --version >/dev/null 2>&1 || true

COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/build/libs/artner-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]