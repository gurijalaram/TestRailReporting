# Prepare runtime.
FROM docker.apriori.com/apriori-jre-base:8 AS runtime
WORKDIR /app

# Prepare build workspace.
FROM docker.apriori.com/apriori-jdk-base:8 AS sdk
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
ARG ASPECTJ_VERSION="1.9.9.1"
COPY --from=build /build-workspace/$FOLDER/$MODULE/build/libs/automation-qa*.jar ./app.jar
COPY --from=build /build-workspace/aspectjweaver-$ASPECTJ_VERSION.jar ./aspectjweaver-$ASPECTJ_VERSION.jar

ENV TOKEN_EMAIL=$TOKEN_EMAIL
ENV MODE=$MODE
ENV PASSWORD=$PASSWORD
ENV TESTS=$TESTS
ENV HEADLESS=$HEADLESS
ENV THREAD_COUNTS=$THREAD_COUNTS
ENV ENVS=$ENVS

CMD java \
  -javaagent:aspectjweaver-1.9.9.1.jar \
  -DthreadCounts=$THREAD_COUNTS \
  -Dmode=$MODE \
  -Dtoken_email=$TOKEN_EMAIL \
  -Dpassword=$PASSWORD \
  -Dheadless=$HEADLESS \
  -Denv=$ENVS \
  -jar app.jar \
  -test $TESTS
