package com.apriori.trr.api.utils;

import com.apriori.trr.api.utils.properties.LoadProperties;

import com.google.common.base.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@AllArgsConstructor
@Data
public class TestRailConfig {
    private String baseApiUrl;
    private String username;
    private String password;
    private Optional<String> applicationName;

    public TestRailConfig(){
        Properties properties = LoadProperties.loadProperties("testrail");
        this.baseApiUrl = properties.getProperty("url").trim();
        this.username  = properties.getProperty("username").trim();
        this.password = properties.getProperty("password").trim();
    }
}
