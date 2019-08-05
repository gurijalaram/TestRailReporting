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
     * @throws UnsupportedEncodingException
     */
    public File getResourceFile(String fileName) throws UnsupportedEncodingException {
        return new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName).getFile(), "UTF-8"));
    }
}