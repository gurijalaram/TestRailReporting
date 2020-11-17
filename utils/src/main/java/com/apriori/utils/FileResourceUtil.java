package com.apriori.utils;

import com.apriori.utils.enums.ProcessGroupEnum;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceUtil.class);
    private static final int TEMP_DIR_ATTEMPTS = 50;
    private static final String S3_BUCKET_NAME = "qa-test-parts";

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
            logger.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get file from cad workspace in S3 bucket
     * The file will be copied inti local temp directory
     * @param processGroup - the file process group
     * @param fileName - the file name
     * @return
     */
    public static File getCloudCadFile(final ProcessGroupEnum processGroup, final String fileName) {
        return copyFileFromCloudToTempFolder("cad", processGroup, fileName);
    }

    /**
     * Get file from common workspace in S3 bucket
     * The file will be copied inti local temp directory
     * @param processGroup - the file process group
     * @param fileName - the file name
     * @return
     */
    public static File getCloudFile(final ProcessGroupEnum processGroup, final String fileName) {
        return copyFileFromCloudToTempFolder("common", processGroup, fileName);
    }

    private static File copyFileFromCloudToTempFolder(final String workspaceName, final ProcessGroupEnum processGroup, final String fileName) {
        final String cloudFilePath = String.format("%s/%s/%s", workspaceName, processGroup.getProcessGroup(), fileName);
        final String localTempFolderPath = String.format("cloud/s3/%s/%s", workspaceName, processGroup.getProcessGroup());

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        S3Object object = s3Client.getObject(new GetObjectRequest(S3_BUCKET_NAME, cloudFilePath));

        return copyIntoTempFile(object.getObjectContent(), localTempFolderPath, fileName);
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
    @Deprecated
    public static File getResourceAsFile(String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceFileName);
            return copyIntoTempFile(in, null, resourceFileName);
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", resourceFileName, e));
        }
    }

    private static File copyIntoTempFile(final InputStream inputStreamOfOriginalFile, final String additionalPath, final String fileName) {
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

    private static File createTempDir(String path) {

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
}