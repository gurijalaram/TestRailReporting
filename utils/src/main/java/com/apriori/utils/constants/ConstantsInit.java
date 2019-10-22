package com.apriori.utils.constants;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:${env}.properties"
})
public interface ConstantsInit extends Config {

    String url();

    @Key("schema.base.path")
    String schemaBasePath();

    @Key("url.internal.api")
    String internalApiURL();

    @Key("url.grid.server")
    String gridServerUrl();

    @Key("url.cid")
    String cidURL();

    @Key("url.cir")
    String cirURL();

    @Key("console.log.level")
    String consoleLogLevelData();

}
