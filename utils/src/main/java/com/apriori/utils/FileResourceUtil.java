package com.apriori.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceUtil.class);

    /**
     * @param fileName - the file name
     * @return file object
     */
    public File getResourceFile(String fileName) {
        File file;

        try {
            file = new File(ClassLoader.getSystemResource(fileName).getFile());
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", fileName, e));
        }
        return file;
    }
}