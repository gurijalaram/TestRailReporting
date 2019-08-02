package main.java.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileResourceUtil {

    /**
     * @param fileName the name of the file
     * @return file object
     * @throws UnsupportedEncodingException
     */
    public File getResourceFile(String fileName) throws UnsupportedEncodingException {
        return new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName).getFile(), "UTF-8"));
    }
}