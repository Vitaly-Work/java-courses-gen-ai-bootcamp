## Table of Content

- [What to do](#what-to-do)
- [Sub-task 1: Enable k8s](#sub-task-1--enable-k8s)
- [Sub-task 2: Deploy containers in k8s](#sub-task-2--deploy-containers-in-k8s)
- [Sub-task 3: Persistent volumes](#sub-task-3--persistent-volumes)
- [Sub-task 4: Stateful Sets](#sub-task-4--stateful-sets)

## What to do
In this module you will create infrastructure for your k8s cluster and deploy your microservices applications there.

## Microservices applications can be found here:
[microservices-intro Github](https://github.com/SiarheiSvila/microservices-intro)


## Sub-task 1: Install k8s
It is recommended to setup Docker and Kubernetes for WSL. Which is hard, overall instructions can be found in [instructions](https://github.com/SiarheiSvila/docker-k8s-setup/blob/master/README.md)
1. Install docker engine (if not installed) as binaries: https://docs.docker.com/engine/install/binaries/.
And make sure docker is running by running `docker --version`
2. Install minikube: https://minikube.sigs.k8s.io/docs/start/.
Verify by running `minikube start`
3. Install kubectl: https://kubernetes.io/docs/tasks/tools/
Verify by running `kubectl version`

_Note_: When using Windows, running PowerShell as administrator may help. Here's the solution to majority of topics I faced:
https://stackoverflow.com/questions/57431890/error-response-from-daemon-hcsshimcreatecomputesystem-the-virtual-machine-co <br />
After docker will start running Hyper-V containers, make sure to run `minikube docker-env | Invoke-Expression`. This command will make minikube runn containers on Hyper-V too

## Sub-task 2: Deploy containers in k8s
In this subtask you need to create manifest `.yml` files with configuration for deployment. These files should contain the next objects:
- Namespace (f.e. k8s-program). All other objects will use this namespace;
- 2 Services (one for each service of your system). Use NodePort service type and configure nodePort field for testing.
- 2 Deployments (one for each service of your system). For apps deployments set `replicas: 2`. You should add environment variables for your applications here.

_Note_: don't forget to specify namespace all objects. <br>
Delete EXPOSE instruction from dockerfiles and upgrade images. <br>
To deploy, run `kubectl apply ./` in folders where yml files are stored.
To view all objects run `kubectl get all -n=<your_namespace>`. <br>
Along with services and deployments, this command outputs pods and replica-sets. **Find out why.**

## Sub-task 3: Persistent volumes
In this subtask you will make your app pods use local storage. This will ensure that no data is lost during pod deploy/redeploy.
1. Add PersistentVolume object with "manual" storage class for the Songs service (create separate manifest file). Configure hostPath field so PersistentVolume create directory on the node.
2. Add PersistenceVolumeClaim objects to your manifest and reference them from Songs deployment object.
3. Test PersistentVolume: create any file inside the container in the volume directory, scale down deployment or delete pod, let replicaset automatically create pod, ensure that file still exists.

## Sub-task 4: Stateful Sets
1. Use StatefulSet object (not Deployment) to create databases. 
2. Configure default storage class "hostpath" for volume claim templates, so allowing k8s to provision storage with default provisioner (PersistentVolume will be created automatically).
3. Create 2 Services (one for each StatefulSet of your system). Use ClusterIP service type to restrict external access.

_Note_: You can also use `kubectl port-forward pod-name 5433:5432` (local machine port:container port) console command to temporarily open access to the database pod <br>
