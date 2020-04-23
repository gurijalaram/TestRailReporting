# Prepare runtime.
FROM openjdk:8-jre-alpine AS runtime
WORKDIR /app

# Prepare build workspace.
FROM gradle:5.3.0-jdk-alpine AS sdk
WORKDIR /build-workspace

# Copy.
COPY . .

# Build.
FROM sdk AS build
ARG ORG_GRADLE_PROJECT_mavenUser
ARG ORG_GRADLE_PROJECT_mavenPassword
RUN gradle clean fatJar

# Test.
FROM build AS test
ARG ORG_GRADLE_PROJECT_mavenUser
ARG ORG_GRADLE_PROJECT_mavenPassword
RUN gradle clean :apitests:test --tests FmsAPISuite -DthreadCount=1 -Denv=cid-aut -Dmode=QA

# App image.
FROM runtime AS final
COPY --from=build /build-workspace/build/libs/automation-qa*.jar ./automation-tests.jar
ENTRYPOINT ["java", "-jar", "automation-tests.jar"]