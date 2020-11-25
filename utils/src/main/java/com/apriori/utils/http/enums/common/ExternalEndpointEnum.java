package com.apriori.utils.http.enums.common;

import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.enums.EndpointEnum;

/**
 * @author kpatel
 */
public interface ExternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return CommonConstants.internalApiURL + String.format(getEndpointString(), variables);
    }

    default String getSchemaLocation() {
        return null;
    }
}
