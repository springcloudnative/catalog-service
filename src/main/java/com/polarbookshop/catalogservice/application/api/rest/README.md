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

IntelliJ Github token: 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

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