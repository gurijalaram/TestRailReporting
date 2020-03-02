package com.apriori.utils.constants;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:${env}/${env}.properties"
})
public interface ConstantsInit extends Config {

    @Key("url.default")
    String url();

    @Key("schema.base.path")
    String schemaBasePath();

    @Key("url.additional.internal.api")
    String internalApiURL();

    @Key("url.additional.cid")
    String cidURL();

    @Key("url.additional.cir")
    String cirURL();

    @Key("url.additional.admin")
    String ciaURL();

    @Key("url.grid.server")
    String gridServerUrl();

    @Key("console.log.level")
    String consoleLogLevelData();

    @Key("users.csv.file")
    String usersCsvFileName();

    @Key("different.users")
    Boolean useDifferentUsers();
  
    @Key("service.host")
    String serviceHost();  
    
    @Key("service.port")
    String servicePort();  
    
    @Key("service.name")
    String serviceName();
    
    @Key("secret.key")
    String secretKey();
    
    @Key("cds.identity.role")
    String cdsIdentityRole();
    
    @Key("cds.identity.user")
    String cdsIdentityUser();
    
    @Key("cds.identity.customer")
    String cdsIdentityCustomer();
    
    @Key("cds.identity.application")
    String cdsIdentityApplication();
}
