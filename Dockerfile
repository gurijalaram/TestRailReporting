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

## Install Chrome
#RUN if [ "$MODULE" = "cid" ] && [ "$TEST_MODE" != "GRID" ]; then \
#   wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
#	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
#	&& apt-get update -qqy \
#	&& apt-get -qqy install google-chrome-stable \
#	&& rm /etc/apt/sources.list.d/google-chrome.list \
#	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
#	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome; \
#	fi
#
## Install Chrome Driver.
#RUN if [ "$MODULE" = "cid" ] && [ "$TEST_MODE" != "GRID" ]; then \
#   wget -q -O /tmp/chromedriver https://chromedriver.storage.googleapis.com/LATEST_RELEASE \
#	&& mv /tmp/chromedriver /usr/bin/chromedriver \
#	&& chown root:root /usr/bin/chromedriver \
#	&& chmod 755 /usr/bin/chromedriver; \
#	fi

##=========
## Firefox
##=========
#ARG FIREFOX_VERSION=esr-latest
#RUN FIREFOX_DOWNLOAD_URL=$(if [ $FIREFOX_VERSION = "latest" ] || [ $FIREFOX_VERSION = "nightly-latest" ] || [ $FIREFOX_VERSION = "devedition-latest" ] || [ $FIREFOX_VERSION = "esr-latest" ]; then echo "https://download.mozilla.org/?product=firefox-$FIREFOX_VERSION-ssl&os=linux64&lang=en-US"; else echo "https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2"; fi) \
#  && apt-get update -qqy \
#  && apt-get -qqy --no-install-recommends install firefox-esr libavcodec-extra \
#  && rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
#  && wget --no-verbose -O /tmp/firefox.tar.bz2 $FIREFOX_DOWNLOAD_URL \
#  && apt-get -y purge firefox-esr \
#  && rm -rf /opt/firefox \
#  && tar -C /opt -xjf /tmp/firefox.tar.bz2 \
#  && rm /tmp/firefox.tar.bz2 \
#  && mv /opt/firefox /opt/firefox-$FIREFOX_VERSION \
#  && ln -fs /opt/firefox-$FIREFOX_VERSION/firefox /usr/bin/firefox
#
##============
## GeckoDriver
##============
#ARG GECKODRIVER_VERSION=0.21.0
#RUN GK_VERSION=$(if [ ${GECKODRIVER_VERSION:-latest} = "latest" ]; then echo "0.27.0"; else echo $GECKODRIVER_VERSION; fi) \
#  && echo "Using GeckoDriver version: "$GK_VERSION \
#  && wget --no-verbose -O /tmp/geckodriver.tar.gz https://github.com/mozilla/geckodriver/releases/download/v$GK_VERSION/geckodriver-v$GK_VERSION-linux64.tar.gz \
#  && rm -rf /opt/geckodriver \
#  && tar -C /opt -zxf /tmp/geckodriver.tar.gz \
#  && rm /tmp/geckodriver.tar.gz \
#  && mv /opt/geckodriver /opt/geckodriver-$GK_VERSION \
#  && chmod 755 /opt/geckodriver-$GK_VERSION \
#  && ln -fs /opt/geckodriver-$GK_VERSION /usr/bin/geckodriver

#=========
# Firefox & GeckoDriver
#=========
ARG firefox_ver=68.0.1
ARG geckodriver_ver=0.22.0

RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends --no-install-suggests \
    ca-certificates \
    && update-ca-certificates \
    \
    # Install tools for building
    && toolDeps=" \
    curl bzip2 \
    " \
    && apt-get install -y --no-install-recommends --no-install-suggests \
    $toolDeps \
    \
    # Install dependencies for Firefox
    && apt-get install -y --no-install-recommends --no-install-suggests \
    `apt-cache depends firefox-esr | awk '/Depends:/{print$2}'` \
    \
    # Download and install Firefox
    && curl -fL -o /tmp/firefox.tar.bz2 \
    https://ftp.mozilla.org/pub/firefox/releases/${firefox_ver}/linux-x86_64/en-GB/firefox-${firefox_ver}.tar.bz2 \
    && tar -xjf /tmp/firefox.tar.bz2 -C /tmp/ \
    && mv /tmp/firefox /opt/firefox \
    \
    # Download and install geckodriver
    && curl -fL -o /tmp/geckodriver.tar.gz \
    https://github.com/mozilla/geckodriver/releases/download/v${geckodriver_ver}/geckodriver-v${geckodriver_ver}-linux64.tar.gz \
    && tar -xzf /tmp/geckodriver.tar.gz -C /tmp/ \
    && chmod +x /tmp/geckodriver \
    && mv /tmp/geckodriver /usr/local/bin/ \
    \
    # Cleanup unnecessary stuff
    && apt-get purge -y --auto-remove \
    -o APT::AutoRemove::RecommendsImportant=false \
    $toolDeps \
    && rm -rf /var/lib/apt/lists/* \
    /tmp/*

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
