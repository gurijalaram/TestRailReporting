package com.apriori.apibase.http.enums.common;

import com.apriori.apibase.http.enums.EndpointEnum;
import com.apriori.utils.constants.Constants;

/**
 * @author kpatel
 */
public interface ExternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return Constants.internalApiURL + String.format(getEndpointString(), ((Object[]) variables));
    }

    default String getSchemaLocation() {
        return null;
    }
}
