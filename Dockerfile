FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
ADD target/*-SNAPSHOT.jar catalog-service.jar
RUN chmod u+r+x catalog-service.jar

ENV JAVA_OPTS=""
# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.2/wait /wait
RUN chmod u+r+x /wait
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /catalog-service.jar" ]