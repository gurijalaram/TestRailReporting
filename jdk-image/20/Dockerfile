FROM gradle:8.3.0-jdk20
WORKDIR /build-workspace

USER root
# Apriori Certs
COPY ../jdk-image/fbc-CONSVDC02-CA-2022.cer /build-workspace/fbc-CONSVDC02-CA-2022.cer
COPY ../jdk-image/fbc1-2040.cer /build-workspace/fbc1-2040.cer

RUN /opt/java/openjdk/bin/keytool -importcert -file /build-workspace/fbc-CONSVDC02-CA-2022.cer -alias fbc-consvdc02-2022 -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

RUN /opt/java/openjdk/bin/keytool -importcert -file /build-workspace/fbc1-2040.cer -alias fbc1-2040 -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt