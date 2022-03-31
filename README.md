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

# Creating a Deployment for a Spring Boot application
A Kubernetes manifest usually comprises four main sections:
* **apiVersion** defines the versioned schema of the specific object representation. Core resources such as Pods or Services follow a versioned schema composed of only a
version number (such as *v1*). Other resources like Deployments or ReplicaSet follow a versioned schema consisting of a group and a version number (for example, apps/v1). If
in doubt about which version to use, you can refer to the Kubernetes documentation (kubernetes.io/docs) or use the kubectl explain <object_name> command to get more
information about the object, including the API version to use.
* **kind** is the type of Kubernetes object you want to create, such as Pod, ReplicaSet, Deployment, or Service. You can use the *kubectl api-resources* command to list all
the objects supported by the cluster.
* **metadata** provides details about the object you want to create, including the name and a set of labels (key/value pairs) used for categorization. For example, you can instruct
Kubernetes to replicate all the objects with a specific label attached.
* **spec** is a section specific to each object type and is used to declare the desired configuration.

The *spec* section of a Deployment manifest contains a *selector* part to define a strategy for identifying which objects should be scaled by a ReplicaSet (more on this later) and a *template*
part describing the specifications for creating the desired Pod and containers.

# Exposing Spring Boot applications with Kubernetes Services
Kubernetes Services let you expose a set of Pods via an interface that other applications can call without knowing the details about the single Pod
instances. This model provides applications with transparent service discovery and load balancing functionality.
There are different types of Services depending on which access policy you want to enforce for the application. The default and most common type is called ClusterIP and exposes a
set of Pods to the cluster. It’s what makes it possible for Pods to communicate with each other (for example, Catalog Service and MySQL).

Four pieces of information characterize a ClusterIP Service:
* the label used to match all the Pods that should be targeted and exposed by the Service (selector);
* the network protocol used by the Service;
* the port on which the Service is listening (we’re going to use port 80 for all Services);
* the targetPort, that is the port exposed by the targeted Pods to which Service will forward requests.

Once the Service is created, we can expose the application to the outside of the cluster relying on the port forwarding feature offered by Kubernetes to expose an
object (in this case, a Service) to your local machine:
```
$ kubectl port-forward service/catalog-service 9001:80
Forwarding from 127.0.0.1:9001 -> 9001
Forwarding from [::1]:9001 -> 9001
```

# Ensuring disposability: Fast startup
Fast startup is relevant in a cloud environment because applications are disposable and frequently created, destroyed, and scaled. The quicker the startup, the sooner a new application instance is
ready to accept connections.
Standard applications like microservices are good with a startup time in the range of a few seconds. On the other hand, serverless applications usually require a faster startup phase in the
range of milliseconds rather than seconds. Spring Boot covers both needs, but the second use case requires some extra work.

# Ensuring disposability: Graceful shutdown
Gracefully shutting down means the application stops accepting new requests, completes all those still in progress, and closes any open resource like database
connections.
All the embedded servers available in Spring Boot support a graceful shutdown mode but in slightly different ways. Tomcat, Jetty, and Netty stop accepting new requests entirely when the
shutdown signal is received. On the other hand, Undertow keeps accepting new requests but immediately replies with an HTTP 503 response.
By default, Spring Boot stops the server immediately after receiving a termination signal (*SIGTERM*). You can switch to a graceful mode by configuring SIGTERM the *server.shutdown* property.
You can also configure the *grace period*, which is how long the application is allowed to process all the pending requests. After the grace period expires, the application is terminated even if there
are still pending requests. By default, the grace period is 30 seconds. You can change it through the *spring.lifecycle.timeout-per-shutdown-phase* property.
**application.yml**
```
server:
    port: 9001
    shutdown: graceful
    tomcat:
        connection-timeout: 2s
        threads:
            max: 50
            min-spare: 5
spring:
    application:
        name: catalog-service
    lifecycle:
        timeout-per-shutdown-phase: 15s
```

After enabling application support for graceful shutdown, you need to update the Deployment manifest accordingly.

The recommended solution is to delay sending the *SIGTERM* signal to the Pod so that Kubernetes has enough time to spread the news across the cluster. By doing so, when the Pod starts the
graceful shutdown procedure, all Kubernetes components already know not to send new requests to it. Technically, the delay can be configured through a *preStop* hook.
**catalog-service/k8s/deployment.yml**
```
apiVersion: apps/v1
kind: Deployment
metadata:
    name: catalog-service
...    

            containers:
            - name: catalog-service
              image: polarbookshop/catalog-service:0.0.1-SNAPSHOT
              imagePullPolicy: Always
              lifecycle:
                preStop:
                    exec:
                        command: [ "sh", "-c", "sleep 5" ]
```

# Local Kubernetes development with Skaffold and Octant
These tools are used to set up a local Kubernetes development workflow to automate steps like building images and applying manifests to a Kubernetes cluster.
It’s part of what is defined as the *inner loop* of working with a Kubernetes platform.
Using Skaffold, you can focus on the business logic of your applications rather than on all those infrastructural concerns, and Octant is used to visualize and 
manage the Kubernetes objects through a convenient GUI.

# Defining a development workflow on Kubernetes with Skaffold
Skaffold is a tool developed by Google that "handles the workflow for building, pushing and deploying your application, allowing you to focus on what matters most: writing code". You can
find instructions on how to install it on the official website: skaffold.dev.
The goal is to design a workflow that will automate the following steps for you:
* packaging a Spring Boot application as a container image using Cloud Native Buildpacks;
* uploading the image to a Kubernetes cluster created with kind;
* creating all the Kubernetes objects described in the YAML manifests;
* enabling the port-forward functionality to access applications from your local computer;
* collecting the logs from the application and showing them in your console.

# Configuring Skaffold for building and deploying
You can initialize Skaffold in a new project using the skaffold init command and choosing a strategy for building the application.
Navigate to the project root folder, and run the following command:
```
$ skaffold init --XXenableBuildpacksInit
```

The resulting configuration will be saved in a *skaffold.yaml* file created in your project root folder. If it doesn’t show up in your IDE, try refreshing the project. So far, we’ve been using the
.yml extension for YAML files. To be consistent, go ahead and rename the Skaffold configuration file to *skaffold.yml*.
The *build* part describes how you want to package the application. The *deploy* part specifies what you want to deploy.

# Deploying applications to K8 with Skaffold
The first option for running Skaffold is the development mode, which builds and deploys the objects you configured in *skaffold.yml*, and then starts watching the project source code.
When something changes, it rolls out the updated objects in your local Kubernetes cluster automatically.
In the project root folder:
```
$ skaffold dev --port-forward
```

The --port-forward flag will set up automatic port forwarding to your local machine. Information on which port is forwarded is printed out at the end of the task.
Unless it’s not available, Skaffold will use the port you defined for the Service object.
When you’re done working with the application, you can terminate the Skaffold process (Ctrl+C), and all the Kubernetes objects will get deleted automatically.
Another option for running Skaffold is using the command. *skaffold run*. It works like the development mode, but it doesn’t provide live-reload nor clean up when it terminates. It’s
typically used in a CI/CD pipeline.

# Asynchronous and non-blocking architectures with Reactor and Spring
The Reactive Manifesto (www.reactivemanifesto.org/) describes a reactive system as responsive, resilient, elastic, and message-driven. Its mission to build loosely-coupled, scalable, resilient, and
cost-effective applications is fully compatible with our definition of cloud native. The new part is achieving that goal by using an asynchronous and non-blocking communication paradigm based
on message-passing.

## From thread-per-request to event loop
Non-reactive applications allocate a thread per request. Until a response is returned, the thread will not be used for anything. This is the *thread-per-request* model.
In the end, this paradigm sets constraints on the application scalability and doesn’t use computational resources in the most efficient way
possible.

Reactive applications are more scalable and efficient by design. Handling requests in a reactive application doesn’t involve allocating a given thread exclusively, but it’s fulfilled
asynchronously based on events. For example, if a database read is required, the thread handling that part of the flow will not wait until data is returned from the database. Instead, a callback is
registered, and whenever the information is ready, a notification is sent, and one of the available threads will execute the callback. During that time, the thread can be used to process other
requests rather than waiting idle.
This paradigm, called *event loop*, doesn’t set hard constraints on the application event loop scalability. It actually makes it easier to scale since an increase in the number of concurrent requests does not
strictly depend on the number of threads. As a matter of fact, a default configuration for reactive applications in Spring is to use only one thread per CPU core. With the non-blocking I/O
capability and a communication paradigm based on events, reactive applications allow more efficient utilization of computational resources.

One of the essential features of reactive applications is to provide non-blocking backpressure (also called *control flow*). It means that consumers can control the amount control flow of data received to
lower the risk of producers sending more data than the consumer can handle and causing a DoS attack, slowing the application, cascading the failure or even leading to a total crash.

The reactive paradigm is a solution to the problem of blocking I/O operations that require more threads to handle high concurrency and may lead to slow or entirely unresponsive applications.
Sometimes the paradigm is mistaken as a way to increase the speed of an application. Reactive is about improving scalability and resilience, not speed.

However, you should also be aware of the additional complexity introduced by such a paradigm. Besides requiring a mindset shift to think in an event-driven way, reactive applications are more
challenging to debug and troubleshoot because of the asynchronous I/O. Before rushing to rewriting all your applications to make them reactive, think twice if that’s necessary and consider
both benefits and drawbacks.

## Project Reactor: Reactive streams with Mono and Flux
Conceptually, reactive streams resemble the Java Stream API in the way of building data pipelines. One of the key differences is that a Java stream is pull-based: consumers process data
in an imperative and synchronous fashion. Instead, reactive streams are push-based: consumers are notified by the producers when new data is available, so the processing happens
asynchronously.
Reactive streams work according to a producer/consumer paradigm. Producers are called *publishers*. They produce data that might be eventually available. Reactor provides publishers two central
APIs implementing the *Producer<T>* interface for objects of type *<T>* and used to compose asynchronous, observable data streams: *Mono<T>* and *Flux<T>*.
Consumers are called *subscriber*s because they subscribe to a publisher and are notified whenever new data is available. As part of the *subscription*, consumers can also define
backpressure by informing the publisher they can process only a certain amount of data at a time.
That is a powerful feature that puts consumers in control of how much data is received, preventing them from being overwhelmed and becoming unresponsive. Reactive streams are
activated only if there’s a subscriber.