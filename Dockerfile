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

# Test.
FROM build AS test
ARG ORG_GRADLE_PROJECT_mavenUser
ARG ORG_GRADLE_PROJECT_mavenPassword
RUN gradle clean :apitests:test --tests FmsAPISuite -DthreadCount=1 -Denv=cid-aut -Dmode=QA

# App image.
FROM runtime AS final
COPY --from=sdk automation-workspace/apitests/build/libs/automation-qa*.jar ./automation-tests.jar
ENTRYPOINT ["java", "-cp", "automation-tests.jar", "org.junit.runner.JUnitCore", "com.apriori.apitests.fms.suite.FmsAPISuite"]
