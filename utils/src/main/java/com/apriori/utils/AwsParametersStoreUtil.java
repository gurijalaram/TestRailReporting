package com.apriori.utils;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SsmException;

@Slf4j
public class AwsParametersStoreUtil extends AwsUtil {

    private static final Region S3_REGION_NAME = Region.US_EAST_1;

    /**
     * Get the parameter value from AWS systems manager -> parameter store
     *
     * @param parameterName Parameter name
     * @return Parameter value
     */
    public static String getSystemParameter(String parameterName) {
        String parameterValue = "";
        SsmClient ssmClient = SsmClient.builder()
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : ProfileCredentialsProvider.create())
            .region(S3_REGION_NAME)
            .build();

        try {
            GetParameterRequest parameterRequest = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true)
                .build();

            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
            parameterValue = parameterResponse.parameter().value();

        } catch (SsmException e) {
            log.error(e.getMessage());
        }
        return parameterValue;
    }
}
