#minikube start --vm-driver=hyperv

cd discovery
docker build -t discovery .

cd ../gateway
docker build -t gateway .

cd ../pc
docker build -t pc .

cd ../po
docker build -t po .

kubectl apply -f api-gateway-service.yaml, eureka-service.yaml, mongo-service.yaml, pc-service.yaml, po-service.yaml, zipkin-service.yaml, api-gateway-deployment.yaml, eureka-deployment.yaml, mongo-deployment.yaml, pc-deployment.yaml, po-deployment.yaml, zipkin-deployment.yaml

#kubectl cluster-info