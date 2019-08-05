package main.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileResourceUtil {

    private final Logger logger = LoggerFactory.getLogger(FileResourceUtil.class);

    /**
     * @param fileName the name of the file
     * @return file object
     */
    public File getResourceFile(String fileName) {
        String decodedFile = null;
        try {
            decodedFile = URLDecoder.decode(ClassLoader.getSystemResource(fileName).getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Unknown encoding" + e);
        }
        return new File(decodedFile);
    }
}