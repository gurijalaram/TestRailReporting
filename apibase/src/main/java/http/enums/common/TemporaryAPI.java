package main.java.http.enums.common;

//TODO z: delete enum
// Created regarding short testing externals API
public interface TemporaryAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {
            return  "http://edc-api.qa.awsdev.apriori.com/" + String.format(getEndpointString(), ((Object[]) variables));
    }

        default String getSchemaLocation() {
            return null;
        }
  }

