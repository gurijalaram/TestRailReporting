# Prepare runtime.
FROM amazoncorretto:11-alpine AS runtime
RUN apk --no-cache add --update bash openssl
USER root
WORKDIR /app

# Prepare build workspace.
FROM 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-jdk-base:11 as sdk
WORKDIR /build-workspace

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
