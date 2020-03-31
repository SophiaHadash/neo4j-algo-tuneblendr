FROM maven:latest
MAINTAINER shadash

WORKDIR /project
ADD src ./src
COPY pom.xml .
CMD mvn clean package