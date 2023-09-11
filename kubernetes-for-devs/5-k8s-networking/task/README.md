# Table of Content

- [What to do](#what-to-do)
- [Sub-task 1: Ingress](#sub-task-1--ingress)

## What to do
In this module you will learn how to install ingress controller and route traffic to your applications. Also, you will practice helm.

## Sub-task 1: Ingress
1. Install ingress controller using helm chart. ([guide](https://kube-workshop.benco.io/08-helm-ingress/))
2. Change Services type to ClusterIP to restrict external access.  
3. Create ingress resource and route your traffic using rules. 
4. Configure rewrite-target of path using annotations. Example routing: from `http://localhost:8080/api/v1/songs` to `http://songs:8080/api/v1`. ([ref docs](https://kubernetes.github.io/ingress-nginx/examples/rewrite/#rewrite-target))