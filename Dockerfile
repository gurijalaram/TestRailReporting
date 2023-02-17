FROM docker.apriori.com/apriori-jre-base:8 AS runtime
WORKDIR /app

# Prepare build workspace.
#FROM gradle:7.4-jdk8 AS sdk
#WORKDIR /build-workspace
#
#USER root
#COPY apriori-https-cert.cer .
#RUN keytool -import -trustcacerts -noprompt \
#    -alias root \
#    -file ./apriori-https-cert.cer \
#    -keystore $JAVA_HOME/jre/lib/security/cacerts \
#    -storepass changeit

FROM docker.apriori.com/apriori-jdk-base:8 AS sdk
USER root

# Copy source code
COPY . .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle clean :$FOLDER:$MODULE:fatJar -x test

# Build & Test.
#FROM runtime as final
FROM runtime as final
ARG FOLDER
ARG MODULE
COPY --from=build /build-workspace/$FOLDER/$MODULE/build/libs/automation-qa*.jar ./app.jar
COPY --from=build /build-workspace/aspectjweaver-1.8.10.jar ./aspectjweaver-1.8.10.jar
#ARG AWS_ACCESS_KEY_ID
#ARG AWS_SECRET_ACCESS_KEY
#ENV AWS_ACCESS_KEY_ID $AWS_ACCESS_KEY_ID
#ENV AWS_SECRET_ACCESS_KEY $AWS_SECRET_ACCESS_KEY

#ARG JAVAOPTS
#ARG FOLDER
#ARG MODULE
#ARG TESTS
#RUN gradle --build-cache --info $JAVAOPTS :$FOLDER:$MODULE:test --tests $TESTS
ENTRYPOINT ["java", "-javaagent:aspectjweaver-1.8.10.jar", "-DthreadCounts=3", "-Dmode=GRID", "-Dtoken_email=cfrith@apriori.com", "-Dpassword=TestEvent2024!", "-Dheadless=true", "-jar", "app.jar", "-test", "testsuites.SanityTestSuite"]
