# Dockerfile dung chung cho cac service. Chon service bang build arg SERVICE.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY user-service user-service
COPY order-service order-service
COPY payment-service payment-service
# Cache thu vien Maven giua cac lan build de build lai nhanh hon (can BuildKit, mac dinh trong Docker moi)
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests package

FROM eclipse-temurin:17-jre
ARG SERVICE
WORKDIR /app
COPY --from=build /build/${SERVICE}/target/${SERVICE}-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
