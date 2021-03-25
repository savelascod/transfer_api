FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} transfer.jar
ENTRYPOINT ["java","-jar","/transfer.jar"]

FROM gradle:6.8.3-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM adoptopenjdk/openjdk11:alpine

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/transfer.jar

ENTRYPOINT ["java","-jar","/app/transfer.jar"]