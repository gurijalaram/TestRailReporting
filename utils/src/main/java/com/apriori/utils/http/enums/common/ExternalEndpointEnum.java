package com.apriori.utils.http.enums.common;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.enums.EndpointEnum;

/**
 * @author kpatel
 */
public interface ExternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return Constants.internalApiURL + String.format(getEndpointString(), variables);
    }

    default String getSchemaLocation() {
        return null;
    }
}
