package com.apriori.trr.api.testrail.http.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class URLFileUtil {

    /**
     * Opens a direct connection to a file url and copies it to a temp folder
     *
     * @param url      - the url
     * @param fileName - the file name
     */
    public void downloadFileFromURL(String url, String fileName) {
        byte[] dataBuffer;

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            FileResourceUtil.copyIntoTempFile(fileOutputStream.toString(), null, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
