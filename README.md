# Run Spring Boot app with Maven wrapper from command line
```
./mvnw spring-boot:run
```

# Run Spring Boot test with Maven wrapper from command line
**./mvnw test -Dtest=<test_class_name>**
```
./mvnw test -Dtest=CatalogServiceApplicationTests
```
# Create a Spring application image using BuildPacks
```
./mvnw spring-boot:build-image
```

# Gradle wrapper command Maven equivalents
```
gradlew build --> mvnw test package
gradlew bootJar --> mvnw spring-boot:run 
```

# Creating images with Dockerfiles
```
docker build -t my-java-image:1.0.0 .
```

**docker build**: Command to build a Docker image
**-t my-java-image:1.0.0**: Name and version of the image to build
**.**: Search for a Dockerfile in the current folder

# Publishing images on Docker Hub
Container images follow common naming conventions which are adopted from all the major registries: *<registry>/<username>/<repository>[:<tag>]*.
1. **Registry hostname**. The hostname for the registry where the image is stored. When using Docker Hub, the registry hostname is docker.io. The Docker Engine will implicitly
prepend the image name with docker.io when you don’t specify one.
2. **Username or repository path**. When using Docker Hub, it will be your Docker username. In other registries, it might be the path to the repository.
3. **Repository and tag**. The repository that contains all the versions of your image. It’s optionally followed by a tag

You have to define an additional name for the image following the conventions required by DockerHub (that is, you
need to tag the image). You can do so with the *docker tag* command.
```
docker tag my-java-image:1.0.0 <your_dockerhub_username>/my-java-image:1.0.0
```
Then, you can finally push it to Docker Hub.
```
docker push <your_dockerhub_username>/my-java-image:1.0.0
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

IntelliJ Github token: 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

# Packaging Spring Boot applications as container images
- Create a network inside which Catalog Service and MySQL can talk to each other using the container name instead of an IP address or a hostname.
```
docker network create catalog-network
```

- Create a Dockerfile in the root of the Spring Boot project. 
- Start a MySQL container, specifying that it should be part of the catalog-network you just created. Using the --net argument, the container will join the
  specified network and rely on the Docker built-in DNS servers:
```
docker run \
--name polar-postgres-catalog \
--net catalog-network \
-e MYSQL_USER=user \
-e MYSQL_PASSWORD=password \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_DATABASE=polardb_catalog \
-p 3306:3306 \
-d mysql:8.0
```

- Build the JAR artifact
```
mvnw package
```
- Build the container image:
```
docker build --build-arg JAR_FILE=target/*.jar -t <your_dockerhub_username>/catalog-service:0.0.1-SNAPSHOT .
```

- Use Snyk to check if it contains any vulnerabilities:
```
docker scan <your_dockerhub_username>/catalog-service:0.0.1-SNAPSHOT --file=Dockerfile
```

- In the Docker Compose add a service for the image created for the Spring Boot application.


# Building container images for production
Spring Boot made that even more efficient by introducing a new way of packaging applications as JAR artifacts: **the layered-JAR** mode. And
since Spring Boot 2.4, that’s the default mode, so you don’t need any extra configuration to use the new functionality.
Applications packaged using the layered-JAR mode are made up of layers, similar to how container images work. This new feature is excellent for building more efficient images. When
using the new JAR packaging, we can expand the JAR artifact and then create a different image layer for each JAR layer. The goal is to have your own classes (changing more frequently) on a
separate layer than the project dependencies (changing less frequently).
By default, Spring Boot applications are packaged as JAR artifacts made up of the following layers, starting from the lowest:
- **dependencies** for all the main dependencies added to the project;
- **spring-boot-loader** for the classes used by the Spring Boot loader component;
- **snapshot-dependencies** for all the snapshot dependencies;
- **application** for your application classes and resources.

# Install kind on Windows
1. Run the following command in an Admin PowerShell window to install chocolatey package:
```
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
```

2. Close the current Admin PowerShell and open another one, or use the refreshenv command. Install docker-desktop and go:
```
choco install docker-desktop -y
choco install golang -y
```

3. Reboot the system.
4. Go ahead and install kind using chocolatey:
```
choco install kind -y
```
This will install kind on your system. With that, either open a new window or run refreshenv. With that done, you can create a kind cluster using:
```
kind create cluster
```

# Running a Spring application on Kubernetes
1. Create the K8s cluster with kind.
2. Tell Kubernetes to deploy the application from a container image. By default, kind uses the Docker Hub
   registry to pull images, and it doesn’t have access to your local ones. Therefore, it will not find
   the image you built. You can manually import it into your local cluster with the following command:
```
kind load docker-image catalog-service:0.0.1-SNAPSHOT
```
3. Now we need a Deployment resource that will instruct Kubernetes to create application instances as Pod resources:
```
kubectl create deployment catalog-service --image=catalog-service:0.0.1-SNAPSHOT
```
**kubectl create**: Created a K8s resource
**deployment**: Type of resource to create
**catalog-service**: Name of the deployment
**--image=catalog-service:0.0.1-SNAPSHOT**: Name and version of the image to run

4. Verifiy the creation of the Deployment:
```
kubectl get deployment
```
By default, applications running in Kubernetes are not accessible. Let’s fix that. First, you can expose the application to the cluster through a Service resource by running the following command:
```
kubectl expose deployment catalog-service --name=catalog-service --port=8080
```
**kubectl expose**: Exposes a K8s resource
**deployment**: Type of resource to create
**catalog-service**: Name of the deployment to expose
**--name=catalog-service**: Name of the service
**--port=8080**: Port number from which the service is exposed

The Service object exposes the application to other components inside the cluster. You can verify it’s been created correctly with the following command:
```
kubectl get service
```
You can then forward the traffic from a local port on your computer (for example, 8000) to the port exposed by the Service inside the cluster ( ). Remember the port mapping 8080 in Docker? It
works in a similar way. The output of the command will tell you if the port forwarding is configured correctly:
```
$ kubectl port-forward service/catalog-service 8000:8080
Forwarding from 127.0.0.1:8000 -> 8080
Forwarding from [::1]:8000 -> 8080
```
**kubectl port-forward**: Command for port-forwarding
**service/catalog-service**: Which resource to expose
**8000**: The port on your localhost
**8080**: The port of the service

# USING PROFILES AS FEATURE FLAGS
This use case for profiles is for loading groups of beans only if the specified profile is active. The deployment environment shouldn’t influence the reasoning behind the grouping too much. A
common mistake is using profiles like **dev** or **prod** to load beans conditionally. If you do that, the application will be coupled with the environment, usually not what we want for a cloud
native application.
The recommendation is using profiles as feature flags when associated with groups of beans to be loaded conditionally. Consider which functionality a profile provides and name it accordingly
rather than thinking about where it will be enabled.
However, there might be cases where a bean handling infrastructural concerns is required in specific platforms. For example, you might have
certain beans that should only be loaded when the application is deployed to a Kubernetes environment (no matter if staging or production). 
In that case, you could define a **kubernetes** profile.

# USING PROFILES AS CONFIGURATION GROUPS
The 15-Factors methodology recommends not batching configuration values into groups named after environments and bundled with the application source code.
The reason is that it wouldn’t scale: as a project grows, new environments might be created for different stages.
The 15-Factor methodology promotes storing configuration in the environment, from the higher to the lowest:
- Defined as CLI argument
- Defined as JVM property
- Defined as environment variable
- Defined in property files
- Default (if any)

Both CLI arguments and JVM properties let you externalize the configuration and keep the application build immutable. However, they require a different command to run the application,
which might result in errors at deployment time. **A better approach is using environment variables**, as recommended by the 15-Factor methodology.
One of the advantages of environment variables is that any operating system supports them, making them portable across any environment. Furthermore, most programming languages
provide features to access environment variables. For example, in Java, you can do that by calling the *System.getenv()* method.
In Spring, you are not required to read environment variables from the surrounding system explicitly. Spring automatically reads them during the startup phase and adds them to the Spring
Environment object, making them accessible, just like any other property. For example, if you run a Spring application in an environment where the MY_ENV_VAR variable is defined, you can
access its value either from the Environment interface or using the @Value annotation.

You can turn a Spring property key into an environment variable by making all letters uppercase and replacing any dot or dash with an underscore. Spring Boot will map it correctly to the
internal syntax. For example, a **POLAR_GREETING** environment variable is recognized as the **polar.greeting** property. This feature is called relaxed binding.
```
$ export POLAR_GREETING="Welcome to the catalog from ENV" && \
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```
On Windows:
```
$ set POLAR_GREETING="Welcome to the catalog from ENV" && \
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

Remove the environment variable from your current Terminal session with the command *unset POLAR_GREETING* (macOS/Linux) or *set POLAR_GREETING=* (Windows).

**You can use environment variables to define values for your configuration data depending on the infrastructure or platform where the application is
deployed, such as profiles, port numbers, IP addresses, and URLs.**

# Containerizing Spring Boot with Cloud Native Buildpacks
Cloud Native Buildpacks (buildpacks.io) is a project hosted by the CNCF to "transform your application source code into images that can run on any cloud".
Some of its features are:
- it auto-detects the type of application and packages it without requiring a Dockerfile;
- it supports multiple languages and platforms;
- it’s highly performant through caching and layering;
- it guarantees reproducible builds;
- it relies on best practices in terms of security;
- it produces production-grade images;
- it supports building native images using GraalVM.

```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <layers>
            <enabled>true</enabled>
        </layers>
        <image>
            <name>${dockerhub_username}/${project.name}:${project.version}</name>
            <env>
                <BP_JVM_VERSION>11</BP_JVM_VERSION>
            </env>
        </image>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>build-image</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Building the image:
```
mvnw spring-boot:build-image -DREGISTRY_URL=docker.io -DREGISTRY_USERNAME=<your_dockerhub_username> -DREGISTRY_TOKEN=<your_dockerhub_token>
```

The Spring Boot plugin lets you use a password to authenticate with the registry, but you should use a token instead. Where can you get one? Go to your account settings on
Docker Hub ( ). There, you’ll find a section named hub.docker.com Security where you can generate an access token.
To use the access token from your Docker CLI client:

1. Run docker login -u ernestoacostacuba

2. At the password prompt, enter the personal access token. (4ce76d6d-c835-4dc9-9877-6c60d583a05f)

# Understanding workflows for building automated pipelines
GitHub actions provide you with a YAML format to describe your automated pipelines that you can compose using the following items as building blocks.

* **Workflow**. A configurable, automated process comprised of one or more *jobs* and defined in a YAML file. You can use a workflow to model continuous integration and continuous
delivery pipelines. Each workflow is configured to be triggered when one or more *events* happen.

* **Event**. The activity that triggers the execution of a workflow. For example, you can define a workflow to be run whenever someone pushes a commit or merge a pull request
to the main branch.

* **Job**. A unit of work in a workflow. Jobs run in parallel by default, but you can configure them to run in sequence by specifying dependencies. For example, you don’t want to
package an application as a Docker image if it fails to compile. A job is made up of one or more *steps*.

* **Step**. An individual task run as part of a job. Steps in a job run sequentially. The commands run by a step are called *actions*.

* **Action**. A command run as part of a step. It’s the smallest unit of work in a workflow. You can use the actions created by the GitHub community or build your own.

* **Runner**. A server that has the GitHub Actions runner applications installed and, therefore, can run jobs. For the Polar Bookshop application, you’ll use a runner hosted on GitHub
and based on Ubuntu, but you can also choose other operating systems or host your own runner.

Workflows should be defined in a *.github/workflows* folder in your Git repository root. You can define one or more workflows.