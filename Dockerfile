FROM openjdk:8-jdk-alpine
MAINTAINER kjoshi007
COPY target/git-repo-1.0.0.jar app-1.0.0.jar
ENTRYPOINT ["java","-jar","/app-1.0.0.jar"]