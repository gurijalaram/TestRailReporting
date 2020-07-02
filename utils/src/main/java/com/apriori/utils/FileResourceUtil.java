package com.apriori.utils;

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
     * Gets resource file from specified path
     *
     * Subfolders should be separated by a comma eg. cad-files, files
     *
     * @param fileName - the file name
     * @return file object
     */
    public static File getResourceCadFile(String fileName) {
        return getResourceAsFile("cad-files",  fileName);
    }

    /**
     * Get file from resource folder
     *
     * Subfolders should be separated by a comma eg. cad-files, files
     *
     * @param resourceFileName - the file name
     * @return file object
     */
    public static File getResourceAsFile(String path, String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path.replace(",", File.separator).trim() + File.separator + resourceFileName);
            if (in == null) {
                return null;
            }

            File tempFile = new File(createTempDir(path), resourceFileName);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (RuntimeException | IOException e) {
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
            if (in == null) {
                return null;
            }

            File tempFile = new File(createTempDir(), resourceFileName);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            logger.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    private static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, "Automation-" + baseName + counter);
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

    private static File createTempDir(String path) {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, "Automation-" + baseName + counter + File.separator + path.replace(",", File.separator).trim());
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