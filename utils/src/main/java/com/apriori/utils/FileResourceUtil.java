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
        String file;

        try {
            new File(ClassLoader.getSystemResource(fileName).getFile());
        } catch (RuntimeException e) {
            throw new CustomFileException(String.format("File with name '%s' does not exist: ", fileName, e));
        }

        file = ClassLoader.getSystemResource(fileName).getFile();
        return new File(file);
    }
}