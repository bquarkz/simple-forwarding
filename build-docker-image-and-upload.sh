#!/usr/bin/env sh
docker build --build-arg JAR_FILE=launcher-app/build/libs/launcher-app-1.0-SNAPSHOT.jar -t simple-forwarding/simple-forwarding:1.0 -f simple-forwarding.dockerFile .
docker login -u="bquarkz"
docker push simple-forwarding/simple-forwarding:1.0
docker logout
