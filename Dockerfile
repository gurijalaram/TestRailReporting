# Prepare runtime.
FROM openjdk:8-jre-stretch AS runtime
WORKDIR /app

# Install Chrome.
RUN curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get -y update
RUN apt-get -y install google-chrome-stable

# Install Chrome Driver.
RUN wget https://chromedriver.storage.googleapis.com/80.0.3987.106/chromedriver_linux64.zip
RUN unzip chromedriver_linux64.zip
RUN mv chromedriver /usr/bin/chromedriver
RUN chown root:root /usr/bin/chromedriver
RUN chmod +x /usr/bin/chromedriver

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

RUN curl -L "https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m)"  -o /usr/local/bin/docker-compose
RUN mv /usr/local/bin/docker-compose /usr/bin/docker-compose
RUN chmod +x /usr/bin/docker-compose
COPY docker-compose.yml .

# Prepare build workspace.
FROM gradle:6.1.1-jdk8 AS sdk
WORKDIR /automation-workspace

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/jre//lib/security/cacerts \
    -storepass changeit

# Copy.
COPY . .

#Build.
WORKDIR /automation-workspace/build
RUN gradle clean fatJar

# App image.
FROM runtime AS final
COPY --from=sdk automation-workspace/uitests/build/libs/automation-qa*.jar ./automation-tests.jar