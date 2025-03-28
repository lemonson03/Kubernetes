##파드와 디플로이먼트 개념에 대한 이해, 관리해보기

## 파드
파드는 쿠버네티스에서 최소한의 단위로 파드 하나가 컨테이너 하나를 담당한다. 컨테이너를 실제 실행하는 리소스가 파드이고, 
우리가 관리하는거는 디플로이먼트(deployment)를 통해 관리한다. 디플로이먼트 내에 파드가 여러개 있고 그 파드가 컨테이
너를 관리하는 방식이다.


커맨드 정리 모음
creating pods
kubectl run hello-kiamol --image=kiamol/ch02-hello-kiamol 
know pods name which is in cluster
kubectl get pods
wait until pod is ready
kubectl wait --for=condition=Ready pod hello-kiamol


컨테이너를 실행하는 책임도 노드가 가짐
컨테이너 런타임 API를 통해 관리

kubectl port-forward pod/hello-kiamol 8080:80 로컬의 8080번 포트에서 들어오는걸 파드의 80번 포트로 전달한다. 

## Application Manifest (Deployment)
kubectl을 이용하면 파드 안에 있는 컨테이너에 접근을 할 수 있는 방법이 있다. 

kubectl get pod hello-kiamol -o custom-columns=NAME:.metadata.name,POD_IP:.status.podIP

파드속 파일 시스템에 접근하는 방법도 있다

## 쿠버네티스 리소스 관리하기 
kubectl get pods 

kubectl delete pods —all

kubectl get pods 

kubectl get deploy 

kubectl delete deploy —all

kubectl get pods 

kubectl get all

파드를 지워도 디플로이먼트가 관리하는 파드면 바로 지워지지 않는다 이때는 디플로이먼트를 지워줘야 다 지울 수 있다. 
