# Prepare build workspace.
FROM gradle:8.2-jdk11 AS sdk

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

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
