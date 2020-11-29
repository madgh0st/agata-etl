#!/usr/bin/env bash


if [[ "$1" == "" ]]; then
    command='deploy'
else
    command=$1
fi

if [[ "$2" == "" ]]; then
    profile='thin'
else
    profile=$2
fi

MVN_REPO_USERNAME=admin
MVN_REPO_PASSWORD=5mC8Jmxv
MVN_REPO='http://192.168.205.104:8081'

MVN_REPO_SNAPSHOT_URL="$MVN_REPO/repository/maven-snapshots/"
MVN_REPO_RELEASE_URL="$MVN_REPO/repository/maven-releases/"
MVN_CENTRAL_URL="$MVN_REPO/repository/maven-central/"
MVN_PUBLIC_URL="$MVN_REPO/repository/maven-public/"

echo 'Выполняем цель: '$command

echo "MVN_REPO_USERNAME=$MVN_REPO_USERNAME"
echo "MVN_REPO_PASSWORD=$MVN_REPO_PASSWORD"
echo "MVN_REPO_SNAPSHOT_URL=$MVN_REPO_SNAPSHOT_URL"
echo "MVN_REPO_RELEASE_URL=$MVN_REPO_RELEASE_URL"
echo "MVN_CENTRAL_URL=$MVN_CENTRAL_URL"
echo "MVN_PUBLIC_URL=$MVN_PUBLIC_URL"

## Build JAR
mvn -s settings.xml -Dmaven.test.skip=true \
    -Drepo.username=${MVN_REPO_USERNAME} \
    -Drepo.password=${MVN_REPO_PASSWORD} \
    -Drepo.snapshot.url=${MVN_REPO_SNAPSHOT_URL} \
    -Drepo.release.url=${MVN_REPO_RELEASE_URL} \
    -Drepo.central.url=${MVN_CENTRAL_URL} \
    -Drepo.public.url=${MVN_PUBLIC_URL} \
    -P$profile \
     $command

ls -1la  target
