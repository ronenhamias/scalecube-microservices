#!/bin/bash

DOCKER_REPO=scalecube/configuration
VERSION=0.0.6-SNAPSHOT
SEED_PORT=4802

docker pull $DOCKER_REPO:scalecube-seed-0.0.6-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-account-0.0.6-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-0.0.6-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-account-gateway-0.0.6-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-gateway-0.0.6-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-gateway-0.0.6-SNAPSHOT

docker-ip() {
  docker inspect --format '{{ .NetworkSettings.IPAddress }}' "$@"
}

docker-run() {
  echo $(docker run -d "$@");
}

CID=$(docker-run $DOCKER_REPO:scalecube-seed-$VERSION)
SEED_IP=$(docker-ip $CID)
echo "seed address: $SEED_IP:4802"

CID=$(docker-run redis)
REDIS_IP=$(docker-ip $CID)
echo "redis address: $REDIS_IP:6379"


docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:$SEED_PORT" -t $DOCKER_REPO:scalecube-seed-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:$SEED_PORT" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t $DOCKER_REPO:scalecube-account-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:$SEED_PORT" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t $DOCKER_REPO:scalecube-configuration-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:$SEED_PORT" -p 8081:8081 -t $DOCKER_REPO:scalecube-account-gateway-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:$SEED_PORT" -p 8082:8081 -t $DOCKER_REPO:scalecube-configuration-gateway-$VERSION

#docker ps --filter "status=exited" | grep '1 days ago' | awk '{print $1}' | xargs --no-run-if-empty docker rm
