# My Operator Readme
This repository contains an example Kubernetes operator written in Java using the Fabric8 Kubernetes Client library. The operator manages custom resources of type MyResource.

## Installation
To install the example operator, follow these steps:

1. Clone this repository: `git clone https://github.com/ovrdoz/k8s-fabric8-operator-java-example.git`
2. Build the Docker image: `docker build -t my-operator:latest .`
3. Push the Docker image to a container registry of your choice.
4. Create a Kubernetes manifest file for your custom resource. For example:
```yaml
cat <<EOF | kubectl -n test apply -f -
apiVersion: operator.example.com/v1
kind: MyResource
metadata:
  name: hello-resource-name
spec:
  namespace: test
  myResourceName: hello-resource-name
  myResourceImage: busybox:1.32
EOF
```
5. Update the image field in the deployment YAML file to point to the Docker image you built and pushed in step 3.
6. Create the Kubernetes deployment and service for the operator: `kubectl apply -f deploy/operator.yaml`
7. Verify that the operator pod is running: `kubectl get pods -n test`
8. Verify that the custom resource was created: `kubectl get myresources -n test`

## Directory Structure
`src/main/java/com/example/operator`: Contains the Java source code for the example operator.
`src/main/java/com/example/operator/k8s`: Contains classes for creating Kubernetes resources.
`src/main/java/com/example/operator/model`: Contains the MyResource class definition.
`Dockerfile`: Defines the Docker image for the example operator.
`pom.xml`: Maven project file.
`deploy/operator.yaml`: Kubernetes manifest for deploying the example operator.

## Contributing
Contributions are welcome! If you find a bug or have an idea for a new feature, please create a new issue or submit a pull request.
