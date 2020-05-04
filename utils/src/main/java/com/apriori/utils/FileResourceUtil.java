package com.apriori.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        return resourceFile("cad-files" + File.separator + fileName);
    }

    private File resourceFile(String fileName) {
        String fileSuffix = fileName.split("\\.", 2)[1];
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (in == null) {
                return null;
            }

            File file = File.createTempFile(String.valueOf(in.hashCode()), ".".concat(fileSuffix));
            file.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(file)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return file;
        } catch (RuntimeException | IOException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", fileName, e));
        }
    }
}