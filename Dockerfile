# 1) Build stage
FROM gradle:8.9.0-jdk21 AS build
WORKDIR /app

# (캐시 최적화) 의존성 정의 파일 먼저 복사
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

# 의존성 캐시를 미리 당겨두기 (실패해도 괜찮게 || true)
RUN gradle --no-daemon dependencies || true

# 소스 복사 후 빌드
COPY . .
RUN gradle clean bootJar --no-daemon

# 2) Run stage (JRE만)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
