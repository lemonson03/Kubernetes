쿠버네티스는 컨테이너를 실행하는 플랫폼이다 

쿠버네티스는 API와 실제로 동작하는 클러스터 이렇게 두가지가 핵심

쿠버네티스는 컨테이너 오케스트레이션 도구이고, 클러스터는 여러 개의 서버 노드로 구성된 하나의 논리적인 단위. 노드 중 일부는 쿠버네티스 API를 실행하며, 나머지 노드는 컨테이너 속에서 동작하는 애플리케이션을 실행

AKS, EKS를 통해 편하게 관리할 수 있다. 

쿠버네티스 클러스터는 애플리케이션을 실행함 YAML 파일에 어플리케이션을 적고 이 파일을 쿠버네티스 API에 전달하면 쿠버네티스가 클러스터 혀재 상태와 비교해서 현재 상태와 파일에 적힌 상태가 다르면 컨테이너를 추가하거나 제거한다. 

도커 이미지와 쿠버네티스 YAML 파일이 있으면 쿠버네티스 클러스터에서 동일하게 동작하는 자기수복형 (self - healing) 애플리케이션을 만들 수 있다. 

애플리케이션을 기술한 YAML 파일은 애플리케이션 매니페스트라 한다. (application manifest)
쿠버네티스 자체의 구성 요소들 역시 리눅스 컨테이너 형태로 실행된다. 

쿠버네티스의 단일 노드 클러스터를 실행할 수 있는 방법은 카인드와 미니큐브가 유명

쿠버네티스 cli 다운

brew install Kubernetes-cli

AKS 다운 받기

brew update && brew install azure-cli

쿠버네티스 AWS에서 EKS 다루기

brew tap weaveworks/tap
brew install weaveworks/tap/eksctl

aws 계정 확인

aws configure

현재 로그인된 aws 계정 확인

aws sts get-caller-identity

이 계정으로 eksctl을 실행해 단일 노드 클러스터를 생성한다. 

두 개의 CPU 코어와 8GB 메모리를 가진 단일 노드 클러스터를 생성

eksctl create cluster --name=kiamol --nodes=1 --node-type=t3. large

kubectl get nodes를 이용해 쿠버네티스 클러스터가 정상 동작중인지를 확인하자
