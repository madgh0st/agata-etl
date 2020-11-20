ARG DOCKER_RH_REGISTRY=docker-gpn.nexign.com/shared
ARG APP_DIR

FROM ${DOCKER_RH_REGISTRY}/ubi7/ubi as base
ARG APP_DIR=/app

COPY  . ${APP_DIR}
RUN mkdir -p ${APP_DIR}/target/classes

FROM ${DOCKER_RH_REGISTRY}/openjdk/openjdk-11-rhel7 as build

USER 185

ARG APP_DIR=/home/jboss/app
ARG YUM_REPOSITORY
ARG MVN_REPO_USERNAME
ARG MVN_REPO_PASSWORD
ARG MVN_REPO_SNAPSHOT_URL
ARG MVN_REPO_RELEASE_URL
ARG MVN_CENTRAL_URL
ARG DOCKER_RH_REGISTRY
 
WORKDIR ${APP_DIR}

COPY --from=base --chown=185:0 /app ${APP_DIR}

SHELL [ "/usr/bin/scl", "enable", "rh-maven36" ]

RUN echo -e "${MVN_REPO_RELEASE_URL}\n${MVN_REPO_SNAPSHOT_URL}\n${MVN_CENTRAL_URL}\n"

## Build JAR
RUN mvn -ntp -B -s .mvn/settings.xml -Dmaven.test.skip=true \
    -Drepo.username=${MVN_REPO_USERNAME} \
    -Drepo.password=${MVN_REPO_PASSWORD} \
    -Drepo.snapshot.url=${MVN_REPO_SNAPSHOT_URL} \
    -Drepo.release.url=${MVN_REPO_RELEASE_URL} \
    -Drepo.central.url=${MVN_CENTRAL_URL} \
     deploy
