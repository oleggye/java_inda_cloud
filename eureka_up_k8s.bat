call minikube -p minikube docker-env

call .\remove_all_k8s.bat

cd ./discovery
docker build --file=Dockerfile --tag=eureka --rm=true .

cd ../gateway
docker build --file=Dockerfile --tag=gateway --rm=true .

cd ../

kubectl apply -f api-gateway-service.yaml
kubectl apply -f api-gateway-deployment.yaml

kubectl apply -f eureka-service.yaml
kubectl apply -f eureka-deployment.yaml

start "dashboard" /B minikube dashboard