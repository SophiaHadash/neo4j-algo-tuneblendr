FROM maven:latest
MAINTAINER shadash

WORKDIR /project
ADD src ./src
COPY pom.xml .
COPY procedure-template.iml .
CMD mvn clean package