# Debugging session

This part of the Kubernetes session will focus on debugging and fixing issues during deployment of applications.
Debugging is something that we do every day when we want to implement or try something new (or just do some changes to our current deployment).
There are three debug tasks that are waiting for you :).

## Tips and commands

- Check Pods in Namespace
  - `kubectl get pod -n NAMESPACE`
  - Add `watch` before `kubectl` to see changes in a loop
    - `watch kubectl get pod -n NAMESPACE`

- Get logs of Pod in Namespace
  - `kubectl logs POD_NAME -n NAMESPACE`
  - With `-f` it will follow the logs of the Pod
  - With `-p` it will show you logs of previous instance of Pod
  - With `-c` you can specify container
  
- Get events of Namespace
  -  `kubectl get events -n NAMESPACE`
  -  Shows events that happened in the Namespace (e.g. container image was pulled, restarting failed container, failed to pull image)
 
- Edit resource
  - `kubectl edit RESOURCE_KIND RESOURCE_NAME -n NAMESPACE`
  - By default VIM/VI editor (based on your default editor installed on your computer)
  - f.e. For editing Deployment
    - `kubectl edit deployment my-app -n default`

- Get resource in YAML format
  - `kubectl get RESOURCE_KIND RESOURCE_NAME -n NAMESPACE -o yaml`
  - f.e. for Deployment
    - `kubectl get deployment my-app -n default -o yaml`

- Describe the resource
  - Similar to the get resource in YAML format
  - More readable
  - `kubectl describe RESOURCE_KIND RESOURCE_NAME -n NAMESPACE -o yaml`

- Get into the running container of Pod to execute commands
  - Really useful in case that you want to cURL to localhost, check environment variables or files that are passed into container
  - `kubectl exec -it POD_NAME -n NAMESPACE -- /bin/bash`
  - In case that Pod contains multiple containers, you need to specify `-c CONTAINER_NAME` as part of the command

- Switch context where is the `kubectl` command looking at
  - In case that you don't want to specify `-n NAMESPACE` in every command, you can switch context to the particular Namespace
  - `kubectl config set-context --current --namespace=NAMESPACE`
  - After that, you can remove the `-n NAMESPACE` from the commands
  - In case that you would like to check different Namespace, but not switch the context, you need to specify `-n NAMESPACE` again

- SSH to Minikube container
  - `minikube ssh`
  - Useful in case that you want to check access to services from the container itself

- Get IP address of Minikube
  - `minikube ip`
  - Useful if you want to point into the container where Minikube is running with containers -> so checking the external access
  - You can use it f.e. in cURL command
    - `curl -X GET $(minikube ip):30080/endpoint`

-----------------
- **Use internet**
  - Not everything is easy to fix without reading articles on the internet
  - In most cases, the issues you are encountering were already encountered by someone else
  - It’s not shame that you don’t know something - everyone started somewhere
  - With each problem you solve, you become more experienced
- Links that can be useful during debug
  - https://quay.io - image registry by RH
  - https://www.base64encode.org/ - encode/decode to/from Base64
  - https://spacelift.io/blog/kubernetes-cheat-sheet - Kubernetes cheat sheet - in case that there is something I didn't mention above :)
-----------------

## Tasks

As I mentioned above, there are three different deployments with different issues that you should try to deploy, debug, and fix :).
To deploy a scenario (or level), you just need to do:

```shell
kubectl apply -f NUM/
```

Where `NUM` is a scenario/level/task ID (you don't say right?).
The following section describes the requirements and expected results for each of the tasks.
It mentions the Namespace where the particular scenario runs.

### Level one

**Task ID**: 1</br>
**Task's directory**: [1](1/)</br>
**Namespace**: level-one</br>
**Description**: This relatively simple Deployment should deploy Busybox image that is available at quay.io and 
echo the "That was easy" sentence and sleep for one hour. However, it seems that the deployment doesn't work.</br>
**Expected result**: The Pod will be in `Running` state, in Pod's logs will be `That was easy` and it will 
just run for an hour (you don't need to wait one hour :D)

### Level two

**Task ID**: 2</br>
**Task's directory**: [2](2/)</br>
**Namespace**: level-two</br>
**Description**: The deployment creates one Pod of NGINX web server, that listens on port 8008. When you get into the NGINX's Pod and try
GET request on localhost:8008, it should output `Hello from the other side!\nWas it easy?\n`. It seems that everything is fine, right?
But as part of the deployment, I created Service object that make the NGINX web server accessible from the outside - and also from other Pods. 
That doesn't seem to work.</br>
**Expected result**:
- NGINX web server will be accessible from the NGINX Pod - `curl -X GET localhost:8008` will work
- It will be accessible also from different Pod - `curl -X GET http://nginx-service.level-two.svc.cluster.local:80` will work
  - `nginx-service` is name of the Service object
  - `level-two` is name of Namespace where the deployment runs
  - `svc.cluster.local` is domain pointing to the "local cluster"
- It will be accessible also from outside - `curl -X GET MINIKUBE_IP:30085` will work as well (from the minikube or podman machine)
- For all of the above it should output `Hello from the other side!\nWas it easy?\n`

### Level three

**Task ID**: 3</br>
**Task's directory**: [3](3/)</br>
**Namespace**: level-three</br>
**Description**: The deployment uses pre-built image `quay.io/lkral/summer-debug` which contains (and runs) simple Python application
from [example-app-debug](../example-app-debug/) directory. It uses three environment variables:

- `USER_NAME` - username
- `PASSWORD` - password
- `STATS_PATH` - path to file with stats in JSON format- defaults to `/config/stats.json` if not set

The username and password are provided by Secret and the stats JSON is provided by ConfigMap, both are passed into the Deployment.
However the deployment seems to be incorrect.</br>
**Expected result**: The user will be logged into the app and the stats stored in the ConfigMap will be printed out.
So the logs of the app's Pod should contain something like this:
```
Welcome back!
Your GH stats:

{
 "contributorStats": {
  "username": "JaneDoe",
  "codingStats": {
   "totalCommits": 350,
   "totalRepositories": 12,
   "openPullRequests": 3,
   "closedIssues": 45
  }
 }
}
```