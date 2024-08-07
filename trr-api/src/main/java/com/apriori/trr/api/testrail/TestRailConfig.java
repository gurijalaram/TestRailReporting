package com.apriori.trr.api.testrail;

import com.apriori.trr.api.testrail.properties.LoadProperties;

import com.google.common.base.Optional;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Configuration for using this client library.
 */
@Slf4j
@Value
@ToString(exclude = {"password"})
public class TestRailConfig {

    private final String baseApiUrl;
    private final String username;
    private final String password;
    private final Optional<String> applicationName;

    TestRailConfig(final String baseApiUrl, final String username, final String password, final String applicationName) {
        this.baseApiUrl = baseApiUrl;
        this.username = username;
        this.password = password;
        this.applicationName = Optional.fromNullable(applicationName);
    }

    public TestRailConfig() {
        Properties properties = LoadProperties.loadProperties("testrail");
        this.baseApiUrl = properties.getProperty("url").trim();
        this.username = properties.getProperty("username").trim();
        this.password = properties.getProperty("password").trim();
        this.applicationName = Optional.fromNullable(properties.getProperty("application").trim());
    }
}
