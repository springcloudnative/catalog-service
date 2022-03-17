# Run Spring Boot app with mvnw from command line
```
./mvnw spring-boot:run
```
# Create a Spring application image using BuildPacks
```
./mvnw spring-boot:build-image
```
# Running a Docker image from command line
```
docker run --rm --name catalog-service -p 8080:8080 catalog-service:0.0.1-SNAPSHOT
```
**docker run**: Runs a container from an image
**--rm**: Removes the container after its execution completes
**--name catalog-service**: Name of the container
**-p 8080:8080**: Expose service outside the container through port 8080
**catalog-service:0.0.1-SNAPSHOT**: Name and version of the image to run