# Prepare runtime.
FROM docker.apriori.com/apriori-jre-base:8 AS runtime
WORKDIR /app

# Prepare build workspace.
FROM docker.apriori.com/apriori-jdk-base:8 AS sdk
USER root

COPY aspectjweaver-1.9.9.1.jar aspectjweaver-1.9.9.1.jar
COPY build.gradle build.gradle
COPY gradle.properties gradle.properties
COPY settings.gradle settings.gradle

COPY apibase/build.gradle apibase/build.gradle
COPY apibase/src apibase/src

COPY integrate/integration/build.gradle integrate/integration/build.gradle
COPY integrate/integration/src integrate/integration/src

COPY microservices microservices
COPY microservices/bcs-api/build.gradle microservices/bcs-api/build.gradle
COPY microservices/bcs-api/src microservices/bcs-api/src
COPY microservices/cis-api/build.gradle microservices/cis-api/build.gradle
COPY microservices/cis-api/src microservices/cis-api/src
COPY microservices/qms-api/build.gradle microservices/qms-api/build.gradle
COPY microservices/qms-api/src microservices/qms-api/src
COPY microservices/cir-api/build.gradle microservices/cir-api/build.gradle
COPY microservices/cir-api/src microservices/cir-api/src
COPY microservices/reporting-api/build.gradle microservices/reporting-api/build.gradle
COPY microservices/reporting-api/src microservices/reporting-api/src
COPY microservices/edc-api/build.gradle microservices/edc-api/build.gradle
COPY microservices/edc-api/src microservices/edc-api/src
COPY microservices/cic-api/build.gradle microservices/cic-api/build.gradle
COPY microservices/cic-api/src microservices/cic-api/src
COPY microservices/cds-api/build.gradle microservices/cds-api/build.gradle
COPY microservices/cds-api/src microservices/cds-api/src
COPY microservices/nts-api/build.gradle microservices/nts-api/build.gradle
COPY microservices/nts-api/src microservices/nts-api/src
COPY microservices/acs-api/build.gradle microservices/acs-api/build.gradle
COPY microservices/acs-api/src microservices/acs-api/src
COPY microservices/dds-api/build.gradle microservices/dds-api/build.gradle
COPY microservices/dds-api/src microservices/dds-api/src
COPY microservices/ats-api/build.gradle microservices/ats-api/build.gradle
COPY microservices/ats-api/src microservices/ats-api/src
COPY microservices/cas-api/build.gradle microservices/cas-api/build.gradle
COPY microservices/cas-api/src microservices/cas-api/src
COPY microservices/cus-api/build.gradle microservices/cus-api/build.gradle
COPY microservices/cus-api/src microservices/cus-api/src
COPY microservices/qds-api/build.gradle microservices/qds-api/build.gradle
COPY microservices/qds-api/src microservices/qds-api/src
COPY microservices/sds-api/build.gradle microservices/sds-api/build.gradle
COPY microservices/sds-api/src microservices/sds-api/src
COPY microservices/cidapp-api/build.gradle microservices/cidapp-api/build.gradle
COPY microservices/cidapp-api/src microservices/cidapp-api/src
COPY microservices/vds-api/build.gradle microservices/vds-api/build.gradle
COPY microservices/vds-api/src microservices/vds-api/src
COPY microservices/fms-api/build.gradle microservices/fms-api/build.gradle
COPY microservices/fms-api/src microservices/fms-api/src
COPY microservices/css-api/build.gradle microservices/css-api/build.gradle
COPY microservices/css-api/src microservices/css-api/src
COPY microservices/dms-api/build.gradle microservices/dms-api/build.gradle
COPY microservices/dms-api/src microservices/dms-api/src

COPY utils/build.gradle utils/build.gradle
COPY utils/src utils/src

COPY web/cir-ui/build.gradle web/cir-ui/build.gradle
COPY web/cir-ui/src web/cir-ui/src
COPY web/cidapp-ui/build.gradle web/cidapp-ui/build.gradle
COPY web/cidapp-ui/src web/cidapp-ui/src
COPY web/edc-ui/build.gradle web/edc-ui/build.gradle
COPY web/edc-ui/src web/edc-ui/src
COPY web/cis-ui/build.gradle web/cis-ui/build.gradle
COPY web/cis-ui/src web/cis-ui/src
COPY web/ciconnect-ui/build.gradle web/ciconnect-ui/build.gradle
COPY web/ciconnect-ui/src web/ciconnect-ui/src
COPY web/auth-ui/build.gradle web/auth-ui/build.gradle
COPY web/auth-ui/src web/auth-ui/src
COPY web/cia-ui/build.gradle web/cia-ui/build.gradle
COPY web/cia-ui/src web/cia-ui/src
COPY web/cas-ui/build.gradle web/cas-ui/build.gradle
COPY web/cas-ui/src web/cas-ui/src

# Build.
FROM sdk as build
ARG FOLDER='web'
ARG MODULE='cidapp-ui'
RUN gradle clean :$FOLDER:$MODULE:fatJar -x test

# Run/Test.
FROM runtime as final
ARG FOLDER='web'
ARG MODULE='cidapp-ui'
COPY --from=build /build-workspace/$FOLDER/$MODULE/build/libs/automation-qa*.jar ./app.jar
COPY --from=build /build-workspace/aspectjweaver-1.9.9.1.jar ./aspectjweaver-1.9.9.1.jar

ENV EMAIL default
ENV MODE docker
ENV PASSWORD default
ENV TEST_SUITE testsuites.SanityTestSuite

CMD java \
  -javaagent:aspectjweaver-1.9.9.1.jar \
  -DthreadCounts=3 \
  -Dmode=$MODE \
  -Dtoken_email=$EMAIL \
  -Dpassword=$PASSWORD \
  -Dheadless=true \
  -jar app.jar \
  -test $TEST_SUITE
