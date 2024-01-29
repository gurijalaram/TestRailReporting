# Prepare build workspace.
FROM 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-jdk-base:11 as sdk
WORKDIR /build-workspace

USER root

# Copy source code
COPY . .

# Build.
FROM sdk as build
ARG FOLDER
ARG MODULE
RUN gradle --build-cache clean :$FOLDER:$MODULE:build -x test

# Build & Test.
FROM build as test

#ARG JAVAOPTS=' -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% "-Dcustomer=HGHG hg HG765HG" -Ddeployment=production'
#ARG JAVAOPTS=' -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% \"-Dcustomer=HGHG hg HG765HG\" -Ddeployment=production'
#ARG JAVAOPTS=" -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% '-Dcustomer=HGHG hg HG765HG' -Ddeployment=production"
#ARG JAVAOPTS=" -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% \"-Dcustomer=HGHG hg HG765HG\" -Ddeployment=production"
#ARG JAVAOPTS=' -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% -Dcustomer=HGHG\ hg\ HG765HG -Ddeployment=production'
#ARG JAVAOPTS=" -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% '-Dcustomer=HGHG hg HG765HG' -Ddeployment=production"
ARG JAVAOPTS=" -Dmode=GRID -Denv=qa-test -DROOT_LOG_LEVEL=DEBUG -Dglobal_use_default_user=true -Dglobal_default_user_name=qa-automation-01@apriori.com -Dglobal_default_password=TrumpetSnakeFridgeToasty18!% \"-Dcustomer=HGHG hg HG765HG\" -Ddeployment=production"
ARG FOLDER
ARG MODULE
ARG TESTS

ENV JAVA_ENV=$JAVAOPTS

RUN echo "**************************************  " + $JAVAOPTS

RUN --mount=type=secret,id=aws_config,target=/root/.aws/config \
    --mount=type=secret,id=aws_creds,target=/root/.aws/credentials \
    gradle --build-cache --info $JAVA_ENV :$FOLDER:$MODULE:test --tests $TESTS
