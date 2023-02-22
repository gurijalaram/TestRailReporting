FROM docker.apriori.com/apriori-jre-base:8 AS runtime
WORKDIR /app

FROM docker.apriori.com/apriori-jdk-base:8 AS sdk
USER root

# Copy source code
COPY . .

# Build.
FROM sdk as build
ARG FOLDER='web'
ARG MODULE='cidapp-ui'
RUN gradle clean :$FOLDER:$MODULE:fatJar -x test

# Build & Test.
#FROM runtime as final
FROM runtime as final
ARG FOLDER='web'
ARG MODULE='cidapp-ui'
ARG MODE
COPY --from=build /build-workspace/$FOLDER/$MODULE/build/libs/automation-qa*.jar ./app.jar
COPY --from=build /build-workspace/aspectjweaver-1.9.9.1.jar ./aspectjweaver-1.9.9.1.jar
ENTRYPOINT ["java", "-javaagent:aspectjweaver-1.9.9.1.jar", "-DthreadCounts=3", "-Dmode=docker", "-Dtoken_email=cfrith@apriori.com", "-Dpassword=TestEvent2024!", "-Dheadless=true", "-jar", "app.jar", "-test", "testsuites.SanityTestSuite"]
