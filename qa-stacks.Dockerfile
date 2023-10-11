# Prepare runtime.
FROM amazoncorretto:11-alpine AS runtime
RUN apk --no-cache add --update bash openssl
USER root
WORKDIR /app

# Prepare build workspace.
# verify gradle version
FROM gradle:8.3.0-jdk11 as sdk
WORKDIR /build-workspace

# Setup build workspace.
USER root

#RUN apt-get update &&  rm -rf /var/lib/apt/lists/*

#RUN mkdir -p /synopsys-detect
#RUN chmod -R 777 /synopsys-detect
#RUN chown -R gradle /synopsys-detect

# Apriori Certs
COPY fbc-CONSVDC02-CA-2022.cer /build-workspace/fbc-CONSVDC02-CA-2022.cer
COPY fbc1-2040.cer /build-workspace/fbc1-2040.cer

RUN /opt/java/openjdk/bin/keytool -importcert -file /build-workspace/fbc-CONSVDC02-CA-2022.cer -alias fbc-consvdc02-2022 -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt
RUN /opt/java/openjdk/bin/keytool -importcert -file /build-workspace/fbc1-2040.cer -alias fbc1-2040 -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

RUN chown -R gradle .
USER root

COPY . .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle clean :$FOLDER:$MODULE:fatJar -x test

# Run/Test.
FROM runtime as final
ARG FOLDER
ARG MODULE
ARG ASPECTJ_VERSION="1.9.20.1"
COPY --from=build /build-workspace/$FOLDER/$MODULE/build/libs/automation-qa*.jar ./app.jar
COPY --from=build /build-workspace/aspectjweaver-$ASPECTJ_VERSION.jar ./aspectjweaver-$ASPECTJ_VERSION.jar

ENV TOKEN_EMAIL=$TOKEN_EMAIL
ENV MODE=$MODE
ENV PASSWORD=$PASSWORD
ENV TESTS=$TESTS
ENV HEADLESS=$HEADLESS
ENV BROWER=$BROWSER
ENV THREAD_COUNTS=$THREAD_COUNTS
ENV ENVS=$ENVS
ENV BASE_URL=$BASE_URL

CMD java \
  -javaagent:aspectjweaver-1.9.20.1.jar \
  -DthreadCounts=$THREAD_COUNTS \
  -Dmode=$MODE \
  -Dtoken_email=$TOKEN_EMAIL \
  -Dpassword=$PASSWORD \
  -Dheadless=$HEADLESS \
  -Dbrowser=$BROWSER \
  -Denv=$ENVS \
  -Dlocal_base_url=$BASE_URL \
  -jar app.jar \
  -test $TESTS
