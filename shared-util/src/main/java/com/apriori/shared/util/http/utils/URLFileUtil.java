package com.apriori.shared.util.http.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class URLFileUtil {

    /**
     * Opens a direct connection to a file url and puts the file in a specified location
     *
     * @param url          - the url
     * @param fileLocation the file location
     */
    public void downloadFileFromURL(String url, String fileLocation) {
        byte[] dataBuffer;

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileLocation)) {
            dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
