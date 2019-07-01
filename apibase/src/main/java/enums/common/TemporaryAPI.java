package main.java.enums.common;

import main.java.constants.Constants;
import main.java.enums.EndpointEnum;

//TODO delete enum
// Created regarding short testing externals API
public interface TemporaryAPI extends EndpointEnum {

        default String getEndpoint(Object... variables) {
            return  "edc-api.atv.awsdev.apriori.com/" + String.format(getEndpointString(), ((Object[]) variables));
        }

        default String getSchemaLocation() {
            return null;
        }
  }

