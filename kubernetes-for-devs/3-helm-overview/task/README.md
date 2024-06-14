# Table of Content

- [What to do](#what-to-do)
- [Sub-task 1: Helm charts](#sub-task-1--helm-chart-default-variables)
- [Sub-task 2: Helm chart helpers](#sub-task-2--helm-chart-helpers)

## What to do
In this module you will learn how to attach persistent storages to your applications. Also, you will understand how helm charts work.

## Sub-task 1: Helm chart default variables
1. Install helm [Official download link](https://helm.sh).
2. Add helm chart to deploy your applications. Make replica-count and namespace a helm values.
3. Add helm values file to store default values for helm variables.
4. Run helm using `helm install` command to deploy applications with default helm variables. Make sure, your applications are up and running.
5. Run helm once again, but this time set namespace and replica-count for `helm intall` to non-default values.

## Sub-task 2: Helm chart helpers
1. Create helm `_helpers.tpl` file and define next labels there: 
   - current date : use helm generator for it's value
   - version
2. Make a config-map use values as labels from helm `_helpers.tpl` file.
