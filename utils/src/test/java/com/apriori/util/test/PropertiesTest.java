package com.apriori.util.test;

import com.apriori.utils.properties.PropertiesContext;

import org.junit.Test;

public class PropertiesTest {

    // INFO: Test example how to use PropertiesContext
    @Test
    public void testProps() {
        String globalProperty = PropertiesContext.get("global.schema_base_path");
        String envProperty = PropertiesContext.get("${env}.secret_key");
        String envGlobal = PropertiesContext.get("env");

        System.out.println(globalProperty);
        System.out.println(envProperty);
        System.out.println(envGlobal);
    }

}
