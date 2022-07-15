FROM adoptopenjdk/openjdk11:latest AS builder
WORKDIR workspace
ARG JAR_FILE=target/*-SNAPSHOT.jar
COPY ${JAR_FILE} catalog-service.jar

RUN java -Djarmode=layertools -jar catalog-service.jar extract

# Creating a new non-root user that will run the application,
# reducing the risks of   #exposing root access to the Docker host.
FROM adoptopenjdk/openjdk11:latest
RUN useradd spring
USER spring
WORKDIR workspace

# The goal is to have your own classes (changing more frequently) on a
# separate layer than the project dependencies (changing less frequently).
# By default, Spring Boot applications are packaged as JAR artifacts made up of the following
# layers, starting from the lowest:
# - dependencies for all the main dependencies added to the project;
# - spring-boot-loader for the classes used by the Spring Boot loader component;
# - snapshot-dependencies for all the snapshot dependencies;
# - application for your application classes and resources.
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./

# Uses the Spring Boot Launcher to start the application from the layers rather than
# an uber-JAR.
CMD [ "sh", "-c", "java org.springframework.boot.loader.JarLauncher" ]
