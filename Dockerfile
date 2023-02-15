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

#RUN chown -R gradle .
#USER gradle

# Copy source code
#COPY . .
COPY apibase/src ./apibase/src
COPY apibase/build.gradle ./apibase/build.gradle

COPY integrate/integration/src ./integrate/integration/src
COPY integrate/integration/build.gradle ./integrate/integration/build.gradle

COPY microservices/acs-api/src ./microservices/acs-api/src
COPY microservices/acs-api/build.gradle ./microservices/acs-api/build.gradle

COPY microservices/acs-api/src ./microservices/acs-api/src
COPY microservices/acs-api/build.gradle ./microservices/acs-api/build.gradle

COPY microservices/ats-api/src ./microservices/ats-api/src
COPY microservices/ats-api/build.gradle ./microservices/ats-api/build.gradle

COPY microservices/bcs-api/src ./microservices/bcs-api/src
COPY microservices/bcs-api/build.gradle ./microservices/bcs-api/build.gradle

COPY microservices/cas-api/src ./microservices/cas-api/src
COPY microservices/cas-api/build.gradle ./microservices/cas-api/build.gradle

COPY microservices/cds-api/src ./microservices/cds-api/src
COPY microservices/cds-api/build.gradle ./microservices/cds-api/build.gradle

COPY microservices/cic-api/src ./microservices/cic-api/src
COPY microservices/cic-api/build.gradle ./microservices/cic-api/build.gradle

COPY microservices/cidapp-api/src ./microservices/cidapp-api/src
COPY microservices/cidapp-api/build.gradle ./microservices/cidapp-api/build.gradle

COPY microservices/cir-api/src ./microservices/cir-api/src
COPY microservices/cir-api/build.gradle ./microservices/cir-api/build.gradle

COPY microservices/cis-api/src ./microservices/cis-api/src
COPY microservices/cis-api/build.gradle ./microservices/cis-api/build.gradle

COPY microservices/css-api/src ./microservices/css-api/src
COPY microservices/css-api/build.gradle ./microservices/css-api/build.gradle

COPY microservices/cus-api/src ./microservices/cus-api/src
COPY microservices/cus-api/build.gradle ./microservices/cus-api/build.gradle

COPY microservices/dds-api/src ./microservices/dds-api/src
COPY microservices/dds-api/build.gradle ./microservices/dds-api/build.gradle

COPY microservices/dms-api/src ./microservices/dms-api/src
COPY microservices/dms-api/build.gradle ./microservices/dms-api/build.gradle

COPY microservices/edc-api/src ./microservices/edc-api/src
COPY microservices/edc-api/build.gradle ./microservices/edc-api/build.gradle

COPY microservices/fms-api/src ./microservices/fms-api/src
COPY microservices/fms-api/build.gradle ./microservices/fms-api/build.gradle

COPY microservices/nts-api/src ./microservices/nts-api/src
COPY microservices/nts-api/build.gradle ./microservices/nts-api/build.gradle

COPY microservices/qds-api/src ./microservices/qds-api/src
COPY microservices/qds-api/build.gradle ./microservices/qds-api/build.gradle

COPY microservices/qms-api/src ./microservices/qms-api/src
COPY microservices/qms-api/build.gradle ./microservices/qms-api/build.gradle

COPY microservices/sds-api/src ./microservices/sds-api/src
COPY microservices/sds-api/build.gradle ./microservices/sds-api/build.gradle

COPY microservices/vds-api/src ./microservices/vds-api/src
COPY microservices/vds-api/build.gradle ./microservices/vds-api/build.gradle

COPY web/auth-ui/src ./web/auth-ui/src
COPY web/auth-ui/build.gradle ./web/auth-ui/build.gradle

COPY web/cas-ui/src ./web/cas-ui/src
COPY web/cas-ui/build.gradle ./web/cas-ui/build.gradle

COPY web/cia-ui/src ./web/cia-ui/src
COPY web/cia-ui/build.gradle ./web/cia-ui/build.gradle

COPY web/ciconnect-ui/src ./web/ciconnec-ui/src
COPY web/ciconnect-ui/build.gradle ./web/ciconnect-ui/build.gradle

COPY web/cidapp-ui/src ./web/cidapp-ui/src
COPY web/cidapp-ui/build.gradle ./web/cidapp-ui/build.gradle

COPY web/cir-ui/src ./web/cir-ui/src
COPY web/cir-ui/build.gradle ./web/cir-ui/build.gradle

COPY web/cis-ui/src ./web/cis-ui/src
COPY web/cis-ui/build.gradle ./web/cis-ui/build.gradle

COPY web/edc-ui/src ./web/edc-ui/src
COPY web/edc-ui/build.gradle ./web/edc-ui/build.gradle

COPY utils/src ./utils/src
COPY utils/build.gradle ./utils/build.gradle

COPY apriori-https-cert.cer .

COPY build.gradle .

COPY settings.gradle .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle clean :$FOLDER:$MODULE:fatJar -x test

# Build & Test.
#FROM build as test
FROM runtime as final
COPY --from=build /build-workspace/web/cidapp-ui/build/libs/automation-qa*.jar ./app.jar
ARG AWS_ACCESS_KEY_ID
#ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
#ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
#
#ARG JAVAOPTS
#ARG FOLDER
#ARG MODULE
#ARG TESTS
#RUN gradle --build-cache --info $JAVAOPTS :$FOLDER:$MODULE:test --tests $TESTS
ENTRYPOINT ["java", "-DthreadCounts=3", "-Dmode=GRID", "-Dtoken_email=cfrith@apriori.com", "-Dpassword=TestEvent2024!", "-Dheadless=true", "-jar", "app.jar", "-test", "testsuites.SanityTestSuite"]
