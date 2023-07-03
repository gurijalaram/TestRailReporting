package com.apriori.utils;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Slf4j
public class AwsParameterStoreUtil extends AwsUtil {

    /**
     * Get the parameter value from AWS systems manager -> parameter store
     *
     * @param parameterName Parameter name
     * @return Parameter value
     */
    public static String getSystemParameter(String parameterName) {
        GetParameterRequest parameterRequest = GetParameterRequest.builder()
            .name(parameterName)
            .withDecryption(true)
            .build();

        GetParameterResponse parameterResponse = getSmmClientInstance()
            .getParameter(parameterRequest);

        return parameterResponse.parameter().value();
    }
}
