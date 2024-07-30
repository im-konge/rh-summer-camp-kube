FROM registry.access.redhat.com/ubi9/ubi-minimal:latest

RUN microdnf update -y \
    && microdnf install java-17-openjdk-headless -y \
    && microdnf clean all

USER root

COPY application/target/spring-app.jar /spring-app.jar

CMD ["java", "-jar", "/spring-app.jar"]