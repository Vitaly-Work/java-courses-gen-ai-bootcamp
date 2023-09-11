# Questions

## 1. What are Kubernetes probes used for?
1) To monitor network traffic to and from the pod
2) To monitor the overall performance of the Kubernetes cluster
3) To monitor the health of the application running inside the pod
4) To monitor the usage of CPU and memory by the pod

## 2. What is the difference between Liveness and Readiness probes in Kubernetes?
1) Liveness probes check if the pod is ready to receive traffic, while Readiness probes check if the pod is healthy
2) Liveness probes check if the pod is healthy, while Readiness probes check if the pod is ready to receive traffic
3) Liveness probes and Readiness probes are interchangeable and can be used interchangeably
4) Readiness probes is not required for a pod to run in Kubernetes, but Liveness probe is required

## 3. What is the recommended storage for environment variables for your Kubernetes pod? What if these variables are sensitive?
1) ConfigMaps; encrypt variables before storing them in ConfigMaps
2) Secrets; encrypt the sensitive variables before storing them in Secrets
3) ConfigMaps; store variables as plain text in ConfigMaps
4) Secrets; store the sensitive variables as plain text in Secrets

## 4. Why should we use deployment strategies in Kubernetes?
1) To ensure high availability and uptime for our applications
2) To reduce the risk of downtime during updates and upgrades
3) To minimize the impact of failed deployments on users
4) All of the above

## 5. What are some examples of deployment strategies in Kubernetes?
1) Rolling update, Blue/Green deployment, Canary deployment
2) GitOps, Jenkins pipeline, Helm charts
3) YAML templates, JSON manifests, Docker images
4) Kubernetes Dashboard, kubectl CLI, Lens IDE

## 6. How can you roll back to the previous version of a deployment in Kubernetes?
1) Delete the current deployment and create a new deployment with the previous version
2) Use the kubectl rollout undo command
3) Modify the YAML file of the current deployment to specify the previous version
4) Stop and restart the Kubernetes cluster

## 7. What are Kubernetes labels used for?
1) To group objects together for easier management and querying
2) To define the amount of CPU and memory resources a pod requires
3) To specify the container image and version to use in a pod
4) To specify the network ports to use in a pod

## 8. What is a Persistent Volume Claim (PVC) in Kubernetes?
1) A request for storage resources by a pod
2) A type of Kubernetes object used to define deployment strategies
3) A YAML file used to create a Kubernetes deployment
4) A security policy that controls access to Kubernetes resources

## 9. What is a Startup Probe in Kubernetes?
1) A type of Kubernetes probe that checks if the application has started correctly
2) A deployment strategy used for new applications in production
3) A YAML file used to define environment variables for a pod
4) A Kubernetes object used to define the networking configuration for a pod

## 10. Which of the following is a valid way to pass a Secret or a ConfigMap to a Pod in Kubernetes?
1) Using a command-line argument
2) Using environment variables
3) Using a file
4) Using a command-line argument and environment variables

[KEYS](https://epam.sharepoint.com/:x:/r/sites/EPAMJavaEducation-Coordinators/Shared%20Documents/Coordinators/Kubernetes%20quiz.xlsx?d=wf6d5c4cdf8f04b4ca6db5b4fb4353fcc&csf=1&web=1&e=FawKQd)