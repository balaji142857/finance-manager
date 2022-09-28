FROM gradle:7.1.0-jdk11 AS builder
ENV APP_HOME=/app/
ENV ARTIFACT_NAME=finance-manager-1.0-SNAPSHOT.jar
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
RUN gradle build || return 0
COPY . .
RUN gradle clean build

FROM openjdk:11
LABEL org.opencontainers.image.authors="balaji142857"
ENV APP_HOME=/app/
ENV ARTIFACT_NAME=finance-manager-1.0-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/$ARTIFACT_NAME .
RUN adduser --system --group spring
USER spring:spring
ARG JAR_FILE=./build/lib/$ARTIFACT_NAME
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]