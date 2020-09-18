package com.apriori.cis.utils;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:cis.properties"
})
public interface CisPropertiesInit extends Config {


    @Key("costing.threads")
    String costingThreads();

    @Key("polling.timeout")
    String pollingTimeout();
}
