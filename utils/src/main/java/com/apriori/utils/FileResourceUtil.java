package com.apriori.utils;

import static com.google.common.io.Files.createTempDir;

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

    /**
     * Gets resource file
     *
     * @param fileName - the file name
     * @return file object
     */
    public File getResourceFile(String fileName) {
        return resourceFile(fileName);
    }

    /**
     * Gets resource file from specified path
     *
     * @param fileName - the file name
     * @return file object
     */
    public File getResourceCadFile(String fileName) {
        return resourceFile("cad-files" + File.separator + fileName);
    }

    private File resourceFile(String fileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (in == null) {
                return null;
            }

            File tempFile = new File(createTempDir(), fileName);
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
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", fileName, e));
        }
    }

    /**
     * Get file from resource folder
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
            GenerateStringUtil.logger.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }
}