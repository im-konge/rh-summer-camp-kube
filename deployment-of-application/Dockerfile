FROM registry.access.redhat.com/ubi9/ubi-minimal:latest

USER root

ARG JAVA_VERSION=17

RUN microdnf update -y \
    && microdnf install java-${JAVA_VERSION}-openjdk-headless shadow-utils -y \
    && microdnf clean all

# Set JAVA_HOME env var
ENV JAVA_HOME /usr/lib/jvm/jre-17
