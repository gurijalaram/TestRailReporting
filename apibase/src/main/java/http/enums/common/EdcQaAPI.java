package main.java.http.enums.common;

import main.java.constants.Constants;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {
            return  Constants.EdcQaURL + String.format(getEndpointString(), ((Object[]) variables));
    }

        default String getSchemaLocation() {
            return null;
        }
  }

