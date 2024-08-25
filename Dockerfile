FROM openjdk:17-jdk-slim
MAINTAINER javacodewiz
COPY target/loan-service-0.0.1-SNAPSHOT.jar loans-service.jar
ENTRYPOINT ["java","-jar","loans-service.jar"]