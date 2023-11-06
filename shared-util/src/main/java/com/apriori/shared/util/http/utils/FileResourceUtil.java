package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.exceptions.ResourceLoadException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SsmException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Slf4j
public class FileResourceUtil {

    private static final int TEMP_DIR_ATTEMPTS = 50;
    private static final String S3_BUCKET_NAME = "qa-test-parts";
    private static final Region S3_REGION_NAME = Region.US_EAST_1;

    /**
     * Get resource file stream from a jar file. {getResource}
     *
     * @param resourceFileName - the file name
     * @return input stream
     */
    public static InputStream getResourceFileStream(String resourceFileName) {
        return ClassLoader.getSystemResourceAsStream(resourceFileName);
    }

    /**
     * Get file from resource folder current module.
     *
     * @param resourceFileName
     * @return file object
     */
    public static File getLocalResourceFile(String resourceFileName) {
        try {
            return new File(
                URLDecoder.decode(
                    ClassLoader.getSystemResource(resourceFileName).getFile(),
                    "UTF-8"
                )
            );
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    @SneakyThrows
    public static File getS3FileAndSaveWithUniqueName(String s3ComponentName, ProcessGroupEnum processGroup) {
        final String uniqueComponentName = new GenerateStringUtil().generateComponentName(s3ComponentName);
        File tempFile = getCloudFile(processGroup, s3ComponentName);
        File newFile = new File(tempFile.getParent(), uniqueComponentName);
        Files.move(tempFile.toPath(), newFile.toPath());

        return newFile;
    }

    /**
     * Get file from cad workspace in S3 bucket
     * The file will be copied inti local temp directory
     *
     * @param processGroup - the file process group
     * @param fileName     - the file name
     * @return
     */
    public static File getCloudCadFile(final ProcessGroupEnum processGroup, final String fileName) {
        return copyFileFromCloudToTempFolder("cad", processGroup, fileName);
    }

    /**
     * Get file from common workspace in S3 bucket
     * The file will be copied inti local temp directory
     *
     * @param processGroup - the file process group
     * @param fileName     - the file name
     * @return
     */
    public static File getCloudFile(final ProcessGroupEnum processGroup, final String fileName) {
        return copyFileFromCloudToTempFolder("common", processGroup, fileName);
    }

    /**
     * Get file from common workspace in S3 bucket
     * The file will be copied inti local temp directory
     *
     * @param workspaceName - subfolder in bucket
     * @return
     */
    public static File getCloudFile(String workspaceName, String fileName) {

        return copyFileFromCloudToTempFolder(workspaceName, fileName);
    }

    public static void uploadCloudFile(final String workspaceName, File fileToUpload) {
        String fileName = fileToUpload.getName();
        final String cloudFilePath = String.format("%s/%s", workspaceName, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(S3_BUCKET_NAME)
            .key(cloudFilePath)
            .acl("public-read")
            .build();

        getS3ClientInstance().putObject(putObjectRequest, RequestBody.fromFile(fileToUpload));
        S3Waiter s3Waiter = getS3ClientInstance().waiter();
        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
            .bucket(S3_BUCKET_NAME)
            .key(cloudFilePath)
            .build();
        WaiterResponse<HeadObjectResponse> waiterResponse = s3Waiter.waitUntilObjectExists(waitRequest);

        waiterResponse.matched().response().ifPresent(System.out::println);
        log.info(String.format("File Uploaded to AWS S3 Bucket  %s/%s", S3_BUCKET_NAME, cloudFilePath));
    }

    /**
     * Delete the file from AWS S3 bucket
     *
     * @param workspaceName - subfolder under s3 bucket
     * @param fileName      name of the file to delete
     */
    public static void deleteCloudFile(String workspaceName, final String fileName) {
        final String cloudFilePath = String.format("%s/%s", workspaceName, fileName);
        final String localTempFolderPath = String.format("cloud/s3/%s/%s", workspaceName, fileName);

        getS3ClientInstance().deleteObject(DeleteObjectRequest.builder()
            .bucket(S3_BUCKET_NAME)
            .key(cloudFilePath).build());
        log.info(String.format("File deleted from AWS S3 Bucket  %s/%s", S3_BUCKET_NAME, cloudFilePath));

    }

    /**
     * Connect to AWS S3 client
     *
     * @return S3Client instance
     */
    private static S3Client getS3ClientInstance() {
        S3Client s3Client = S3Client.builder()
            .region(S3_REGION_NAME)
            .credentialsProvider(System.getenv("AWS_ACCESS_KEY_ID") != null
                ? EnvironmentVariableCredentialsProvider.create()
                : ProfileCredentialsProvider.create()
            )
            .build();
        return s3Client;
    }

    /**
     * connect to AWS S3 bucket and copy the file from S3 bucket to local temp directory
     *
     * @param workspaceName - subfolder name under S3 bucket
     * @param processGroup  - subforder name under workspace name
     * @param fileName      - name of the file
     * @return File
     */
    private static File copyFileFromCloudToTempFolder(final String workspaceName, final ProcessGroupEnum processGroup, final String fileName) {
        final String cloudFilePath = String.format("%s/%s/%s", workspaceName, processGroup.getProcessGroup(), fileName);
        final String localTempFolderPath = String.format("cloud/s3/%s/%s", workspaceName, processGroup.getProcessGroup());

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(S3_BUCKET_NAME)
            .key(cloudFilePath)
            .build();

        ResponseInputStream<GetObjectResponse> object = getS3ClientInstance().getObject(getObjectRequest);

        return copyIntoTempFile(object, localTempFolderPath, fileName);
    }

    /**
     * connect to AWS S3 bucket and copy the file from S3 bucket to local temp directory
     * using folder and file name as parameters
     *
     * @param workspaceName - subfolder under S3 bucket
     * @param fileName      - name of the file to copy
     * @return File
     */
    private static File copyFileFromCloudToTempFolder(String workspaceName, String fileName) {
        final String cloudFilePath = String.format("%s/%s", workspaceName, fileName);
        final String localTempFolderPath = String.format("cloud/s3/%s/%s", workspaceName, fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(S3_BUCKET_NAME)
            .key(cloudFilePath)
            .build();

        ResponseInputStream<GetObjectResponse> object = getS3ClientInstance().getObject(getObjectRequest);

        return copyIntoTempFile(object, localTempFolderPath, fileName);
    }

    /**
     * Gets resource file from specified path
     * <p>
     * Subfolders should be separated by a comma eg. a folder structure of [cad-file > multipartfiles > files] would be represented as "cad-files, multipartfiles, files"
     *
     * @param fileName - the file name
     * @return file object
     */
    @Deprecated
    public static File getResourceCadFile(String fileName) {
        return getResourceAsFile("cad-files", fileName);
    }

    /**
     * Get file from resource folder
     * <p>
     * Subfolders should be separated by a comma eg. cad-files, files
     *
     * @param resourceFileName - the file name
     * @return file object
     */
    @Deprecated
    public static File getResourceAsFile(String path, String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path.replace(",", File.separator).trim() + File.separator + resourceFileName);

            return copyIntoTempFile(in, path, resourceFileName);
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", resourceFileName, e));

        }
    }

    /**
     * Get file from resource folder
     *
     * @param resourceFileName - the file name
     * @return file object
     */
    public static File getResourceAsFile(String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceFileName);
            return copyIntoTempFile(in, null, resourceFileName);
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", resourceFileName, e));
        }
    }

    public static File copyIntoTempFile(final InputStream inputStreamOfOriginalFile, final String additionalPath, final String fileName) {
        if (inputStreamOfOriginalFile == null) {
            return null;
        }

        File tempFile = new File(createTempDir(additionalPath), fileName);
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            //copy stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStreamOfOriginalFile.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return tempFile;

        } catch (RuntimeException | IOException e) {
            throw new ResourceLoadException(String.format("File with name '%s' can not be saved: ", fileName, e));
        }

    }

    public static File copyIntoTempFile(final String contentBytes, final String additionalPath, final String fileName) {
        File tempFile = new File(createTempDir(additionalPath), fileName);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] data = Base64.getMimeDecoder().decode(contentBytes.getBytes(StandardCharsets.UTF_8));
            out.write(data);
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("File with name '%s' can not be saved: ", fileName, e));
        }
        return tempFile;
    }

    public static File createTempDir(String path) {

        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        String childFilePath = "Automation-" + baseName + "%s";

        if (path != null) {
            childFilePath = childFilePath + File.separator + path.replace(",", File.separator).trim();
        }

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, String.format(childFilePath, counter));

            if (tempDir.mkdirs()) {
                return tempDir;
            }
        }
        throw new IllegalStateException(
            "Failed to create directory within "
                + TEMP_DIR_ATTEMPTS
                + " attempts (tried "
                + baseName
                + "0 to "
                + baseName
                + (TEMP_DIR_ATTEMPTS - 1)
                + ')');
    }

    /**
     * Read CSV file from AWS S3 bucket
     *
     * @param workspaceName - Name of the folder under S3 bucket
     * @param filename      - file to be read
     * @return List of Strings
     */
    public static List<String[]> getCloudFileContent(String workspaceName, String filename) {
        File file = getCloudFile(workspaceName, filename);
        List<String[]> fileData = null;
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(1)
                .build();
            fileData = csvReader.readAll();
            fileReader.close();
            csvReader.close();
        } catch (Exception e) {
            log.error(String.format("FILE NOT FOUND ::: %s", e.getMessage()));
        }
        return fileData;
    }

    /**
     * Convert JSON file into string
     *
     * @param file
     * @return String
     * @throws Exception
     */
    public static String convertFileIntoString(String file) {
        // declare a variable in which we store the JSON data as a string and return it to the main() method
        String jsonFileContent = null;
        try {
            jsonFileContent = new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return jsonFileContent;
    }

    /**
     * Wait certain time to check if file exists(appear)  - if exists - delete it
     *
     * @param path          - path to the file
     * @param waitTimeInSec - how long wait to appear
     * @throws Exception
     */
    public static Boolean deleteFileWhenAppears(Path path, Integer waitTimeInSec) {
        long initialTime = System.currentTimeMillis() / 1000;
        do {
            try {
                Thread.sleep(200);
                if (Files.deleteIfExists(path)) {
                    log.info("File was removed. File path: {}", path);
                    return true;
                }
            } catch (IOException | InterruptedException e) {
                log.error("Failed to remove file.");
                throw new IllegalArgumentException(e);
            }
        } while (((System.currentTimeMillis() / 1000) - initialTime) < waitTimeInSec);

        return false;
    }

    /**
     * Get the parameter value from AWS systems manager -> parameter store
     *
     * @param parameterName Parameter name
     * @return Parameter value
     */
    public static String getAwsSystemParameter(String parameterName) {
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

    /**
     * Reads a file from an input stream as string
     *
     * @param fileName - the file name
     * @return String
     */
    @SneakyThrows
    public static String readFileToString(String fileName) {
        return FileUtils.readFileToString(FileResourceUtil.getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }
}