FROM maven:3.8.6-openjdk-11-slim AS builder
ENV DEBIAN_FRONTEND noninteractive

RUN mkdir -p /app/demo-logging
WORKDIR /app
COPY ./ /app/demo-logging/
WORKDIR /app/demo-logging

RUN mvn -Dmaven.test.skip=true clean package

FROM openjdk:11-jre-slim
ENV DEBIAN_FRONTEND noninteractive

RUN mkdir -p /app/logs
WORKDIR /app

COPY --from=builder /app/demo-logging/target/demo-logging-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/demo-logging/timezone /etc
EXPOSE 8080/tcp

ENTRYPOINT ["java","-jar","demo-logging-0.0.1-SNAPSHOT.jar"]