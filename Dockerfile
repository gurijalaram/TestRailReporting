# Prepare runtime.
FROM openjdk:8-jre-alpine AS runtime
WORKDIR /app

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

# Prepare build workspace.
FROM gradle:6.1.1-jdk8 AS sdk
WORKDIR /automation-workspace

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/jre//lib/security/cacerts \
    -storepass changeit

# Copy.
COPY . .

#Build.
WORKDIR /automation-workspace/build
RUN gradle clean fatJar

# App image.
FROM runtime AS final
ARG MODULE=apitests
COPY --from=sdk automation-workspace/$MODULE/build/libs/automation-qa*.jar ./automation-tests.jar