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

ARG JAVAOPTS
ARG FOLDER
ARG MODULE
ARG TESTS
ARG AWS_PROFILE_ARG
ARG AWS_REGION_ARG


ENV AWS_PROFILE=$AWS_PROFILE_ARG
ENV AWS_REGION=$AWS_REGION_ARG

RUN --mount=type=secret,id=aws_config,target=/root/.aws/config \
    --mount=type=secret,id=aws_creds,target=/root/.aws/credentials \
    gradle --build-cache --info $JAVAOPTS :$FOLDER:$MODULE:test --tests $TESTS
