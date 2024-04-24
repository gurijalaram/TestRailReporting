# Prepare build workspace.
FROM 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-jdk-base:17 as sdk
WORKDIR /build-workspace

USER root

# Copy source code
COPY . .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle --build-cache --quiet clean :$FOLDER:$MODULE:build -x test

# Build & Test.
FROM build as test

ARG JAVAOPTS
ARG FOLDER
ARG MODULE
ARG TESTS

ENV JAVA_OPTS="$JAVA_OPTS $JAVAOPTS"

RUN --mount=type=secret,id=aws_config,target=/root/.aws/config \
    --mount=type=secret,id=aws_creds,target=/root/.aws/credentials \
    gradle --build-cache --quiet :$FOLDER:$MODULE:test --tests $TESTS