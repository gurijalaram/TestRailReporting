# Prepare build workspace.
FROM gradle:8.3.0-jdk11 AS sdk

USER root
COPY ../jdk-image/fbc1-2040.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./fbc1-2040.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit
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
ARG AWS_ACCESS_KEY_ID
ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY

ARG JAVAOPTS
ARG FOLDER
ARG MODULE
ARG TESTS

RUN gradle --build-cache --info $JAVAOPTS :$FOLDER:$MODULE:test --tests $TESTS
