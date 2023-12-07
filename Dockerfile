# Prepare build workspace.
FROM 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-jdk-base:11 as sdk
WORKDIR /build-workspace

USER root

# Copy source code
COPY . .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle --build-cache clean :$FOLDER:$MODULE:build -x test

# Build & Test.
FROM build as test

ARG AWS_CONFIG_SECRET_TXT
ARG AWS_CREDENTIALS_SECRET_TXT
ARG JAVAOPTS
ARG FOLDER
ARG MODULE
ARG TESTS

COPY AWS_CONFIG_SECRET_TXT "/root/.aws/config"
COPY AWS_CREDENTIALS_SECRET_TXT "/root/.aws/credentials"

RUN gradle --build-cache --info $JAVAOPTS :$FOLDER:$MODULE:test --tests $TESTS
