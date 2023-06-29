package com.apriori.utils;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsUtil {

    protected static final String S3_BUCKET_NAME = "qa-test-parts";
    protected static final Region S3_REGION_NAME = Region.US_EAST_1;

    /**
     * Connect to AWS S3 client
     *
     * @return S3Client instance
     */
    protected static S3Client getS3ClientInstance() {
        S3Client s3Client = S3Client.builder()
            .region(S3_REGION_NAME)
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : ProfileCredentialsProvider.create()
            )
            .build();
        return s3Client;
    }
}
