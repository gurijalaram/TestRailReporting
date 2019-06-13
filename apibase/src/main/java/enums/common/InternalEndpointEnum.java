package main.java.enums.common;

import main.java.constants.Constants;
import main.java.enums.EndpointEnum;

/**
 * @author kpatel
 */
public interface InternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return Constants.internalApiURL + String.format(getEndpointString(), ((Object[]) variables));
    }
}
