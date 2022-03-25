FROM adoptopenjdk/openjdk11:latest

# Build argument specifying the location of the application JAR file in the project
ARG JAR_FILE=target/*-SNAPSHOT.jar

# Copy the application JAR file from the local machine into the image
COPY ${JAR_FILE} catalog-service.jar

# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

# Set the container entry point to run the application
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /catalog-service.jar" ]