package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ssm.SsmClient;

public class AwsUtil {

    protected static final String S3_BUCKET_NAME = "qa-test-parts";
    protected static final Region S3_REGION_NAME = System.getenv("AWS_REGION") != null
        ? Region.of(System.getenv("AWS_REGION")) : Region.US_EAST_1;

    /**
     * Configure instance for AWS Systems Manager
     * Use AWS authorization type based on running environment,
     * to download parameters from AWS Parameters Store
     *
     * @return SsmClient configured instance with appropriate credentials
     */
    protected static SsmClient getSmmClientInstance() {
        return SsmClient.builder()
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : getProfileCredentialsProvider()
            )
            .region(S3_REGION_NAME)
            .build();
    }

    /**
     * Configure instance for AWS S3
     * Use AWS authorization type based on running environment,
     * to download files from S3 bucket
     *
     * @return S3Client configured instance with appropriate credentials
     */
    protected static S3Client getS3ClientInstance() {
        return S3Client.builder()
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : ProfileCredentialsProvider.create()
                // TODO: a profile should be generated based on the environment, but qa Jenkins doesn't has an access for staging AWS profile
                //getProfileCredentialsProvider()
            )
            .region(S3_REGION_NAME)
            .build();
    }

    /**
     * Get aws staging profile, if environment is staging and it is not local run
     *  else
     * get default aws profile
     * @return
     */
    private static ProfileCredentialsProvider getProfileCredentialsProvider() {
        switch (PropertiesContext.get("env")) {
            case "staging" : return ProfileCredentialsProvider.create("staging");
            case "production" : return ProfileCredentialsProvider.create("production");
            default: return ProfileCredentialsProvider.create();
        }
    }

    private static boolean isLocalRun() {
        final String mode = System.getProperty("mode");
        return mode == null || StringUtils.isEmpty(mode) || mode.equalsIgnoreCase("QA_LOCAL");
    }
}
