package com.apriori.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceUtil.class);

    private File file;

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
        return resourceFile("cad-files" + fileName);
    }

    private File resourceFile(String fileName) {
        try {
            file = new File(ClassLoader.getSystemResource(fileName).getFile());
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", fileName, e));
        }
        return file;
    }
}