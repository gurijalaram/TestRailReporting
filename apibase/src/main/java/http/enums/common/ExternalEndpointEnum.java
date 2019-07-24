package main.java.http.enums.common;

import main.java.constants.Constants;
import main.java.http.enums.EndpointEnum;

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
