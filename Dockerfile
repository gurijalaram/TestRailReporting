# Prepare runtime.
FROM openjdk:8-jre-stretch AS runtime
ARG MODULE
ARG TEST_MODE
WORKDIR /app

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

# Install Chrome
RUN if [ "$MODULE" = "cid" ] && [ "$TEST_MODE" != "GRID" ]; then \
   wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
	&& apt-get update -qqy \
	&& apt-get -qqy install google-chrome-stable \
	&& rm /etc/apt/sources.list.d/google-chrome.list \
	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome; \
	fi

# Install Chrome Driver.
RUN if [ "$MODULE" = "cid" ] && [ "$TEST_MODE" != "GRID" ]; then \
   wget -q -O /tmp/chromedriver https://chromedriver.storage.googleapis.com/LATEST_RELEASE \
	&& mv /tmp/chromedriver /usr/bin/chromedriver \
	&& chown root:root /usr/bin/chromedriver \
	&& chmod 755 /usr/bin/chromedriver; \
	fi

# Prepare build workspace.
FROM gradle:6.1.1-jdk8 AS sdk
WORKDIR /automation-workspace

USER root
COPY apriori-https-cert.cer .
RUN keytool -import -trustcacerts -noprompt \
    -alias root \
    -file ./apriori-https-cert.cer \
    -keystore $JAVA_HOME/jre/lib/security/cacerts \
    -storepass changeit

# Copy source code
COPY . .

# Build.
WORKDIR /automation-workspace/build
RUN gradle clean fatJar

# App image.
FROM runtime AS final
ARG MODULE
COPY --from=sdk automation-workspace/$MODULE/build/libs/automation-qa*.jar ./automation-tests.jar
