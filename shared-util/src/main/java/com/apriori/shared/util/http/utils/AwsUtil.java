package com.apriori.shared.util.http.utils;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

public class AwsUtil {

    protected static final String S3_BUCKET_NAME = "qa-test-parts";
    protected static final Region S3_REGION_NAME =System.getenv("AWS_REGION") != null ?
        Region.of(System.getenv("AWS_REGION")) : Region.US_EAST_1;

    /**
     * Configure instance for AWS Systems Manager
     * Use AWS authorization type based on running environment
     *
     * @return SsmClient configured instance with appropriate credentials
     */
    protected static SsmClient getSmmClientInstance() {
        return SsmClient.builder()
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : ProfileCredentialsProvider.create())
            .region(S3_REGION_NAME)
            .build();
    }

    /**
     * Configure instance for AWS S3
     * Use AWS authorization type based on running environment
     *
     * @return S3Client configured instance with appropriate credentials
     */
    protected static S3Client getS3ClientInstance() {


        String roleSessionName = "WBCSession-" + Thread.currentThread().getId();
        AwsCredentialsProvider awsCredentialsProvider = roleCredentialsProvider("apriori-central", roleSessionName);

        return S3Client.builder()
            .region(S3_REGION_NAME)
            .credentialsProvider(
                // System.getenv("AWS_ACCESS_KEY_ID") != null
                //? EnvironmentVariableCredentialsProvider.create()
                awsCredentialsProvider
            )
            .build();
    }

    private static AwsCredentialsProvider roleCredentialsProvider(String roleArn, String roleSessionName) {
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
            .roleArn(roleArn)
            .roleSessionName(roleSessionName)
            .build();

        StsClient stsClient = StsClient.builder().region(Region.US_EAST_1).build();

        return StsAssumeRoleCredentialsProvider
            .builder()
            .stsClient(stsClient).refreshRequest(assumeRoleRequest)
            .asyncCredentialUpdateEnabled(true)
            .build();

    }
}
