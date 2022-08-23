package com.apriori.cnh.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CNHAPIEnum implements ExternalEndpointEnum {

    EXECUTE("dev/customers/3KED5H5BKN85/reports/report-1/execute");


    private final String endpoint;

    CNHAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

//    @Override
//    public String getEndpoint(Object... variables) {
//        return PropertiesContext.get("${env}.vds.api_url") + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
//    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cnh.api_url") + String.format(getEndpointString(), variables);
    }

    private String addQuery(String endpointString) {
        String querySymbol = "?";

        if (endpointString.contains("?")) {
            querySymbol = "&";
        }

        return querySymbol + "key=" + PropertiesContext.get("${env}.secret_key");

    }

}
