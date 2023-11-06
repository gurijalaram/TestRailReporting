package com.apriori.cic.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum PlmApiEnum implements ExternalEndpointEnum {

    PLM_WC_SEARCH("/servlet/rest/search/objects"),
    PLM_WC_CSRF_TOKEN("/servlet/odata/PTC/GetCSRFToken"),
    PLM_WC_PROD_MGMT_PARTS("/servlet/odata/ProdMgmt/Parts('%s')"),
    PLM_WC_PROD_MGMT_PART_CHECKOUT("/servlet/odata/ProdMgmt/Parts('%s')/PTC.ProdMgmt.CheckOut"),
    PLM_WC_PROD_MGMT_PART_CHECKIN("/servlet/odata/ProdMgmt/Parts('%s')/PTC.ProdMgmt.CheckIn");

    private final String endpoint;

    PlmApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return getPlmUrl() + String.format(getEndpointString(), variables);
    }

    public String getPlmUrl() {
        return PropertiesContext.get("ci-connect.windchill.host_name");
    }
}
