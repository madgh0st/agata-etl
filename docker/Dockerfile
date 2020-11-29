ARG DOCKER_REGISTRY=registry.access.redhat.com
ARG APP_DIR

FROM ${DOCKER_REGISTRY}/ubi7/ubi as base
ARG APP_DIR=/app

COPY  . ${APP_DIR}
RUN mkdir -p ${APP_DIR}/target

FROM ${DOCKER_REGISTRY}/openjdk/openjdk-11-rhel7 as build

USER 185

ENV APP_DIR=/home/jboss/app
ENV MVN_REPO_USERNAME=admin
ENV MVN_REPO_PASSWORD=5mC8Jmxv
ARG MVN_REPO_SNAPSHOT_URL=http://192.168.205.104:8081/repository/maven-snapshots/
ARG MVN_REPO_RELEASE_URL=http://192.168.205.104:8081/repository/maven-releases/
ARG MVN_CENTRAL_URL=http://192.168.205.104:8081/repository/maven-central/
ARG MVN_PUBLIC_URL=http://192.168.205.104:8081/repository/maven-public/
 
WORKDIR ${APP_DIR}

COPY --from=base --chown=185:0 /app ${APP_DIR}

SHELL [ "/usr/bin/scl", "enable", "rh-maven36" ]

RUN echo -e "${MVN_REPO_RELEASE_URL}\n${MVN_REPO_SNAPSHOT_URL}\n${MVN_CENTRAL_URL}\n"

## Build JAR
RUN mvn -s settings.xml -Dmaven.test.skip=true \
    -Drepo.username=${MVN_REPO_USERNAME} \
    -Drepo.password=${MVN_REPO_PASSWORD} \
    -Drepo.snapshot.url=${MVN_REPO_SNAPSHOT_URL} \
    -Drepo.release.url=${MVN_REPO_RELEASE_URL} \
    -Drepo.central.url=${MVN_CENTRAL_URL} \
     deploy
