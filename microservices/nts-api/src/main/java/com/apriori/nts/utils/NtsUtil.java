package com.apriori.nts.utils;

import com.apriori.utils.json.utils.JsonManager;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NtsUtil {


    public static Object postMultiPartFormData(String uri, Map<String, String> params, Class klass, String cloudContext) throws IOException {
        return postMultiPartFormData(uri, params, klass, null, cloudContext);

    }

    public static Object postMultiPartFormData(String uri, Map<String, String> params, Class klass, File file, String cloudContext)
        throws IOException {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        String boundary = "----------------------------544615151549871231842369";
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type",
            "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("ap-cloud-context", cloudContext);

        OutputStream outputStream = conn.getOutputStream();
        BufferedWriter bodyWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        for (Object key : params.keySet()) {
            bodyWriter.write("--" + boundary + "\r\n");
            bodyWriter.write("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + params.get(key));
            bodyWriter.write("\r\n");
            bodyWriter.flush();
        }

        if (file != null) {

            bodyWriter.write("--" + boundary + "\r\n");
            bodyWriter.write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
                + file.getName() + "\"");
            bodyWriter.write("\r\n\r\n");
            bodyWriter.flush();

            InputStream istreamFile = new FileInputStream(file);
            Integer bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = istreamFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        }


        bodyWriter.write("--" + boundary + "-\r\n");
        bodyWriter.flush();

        outputStream.close();
        bodyWriter.close();

        int status = conn.getResponseCode();

        InputStream inputStream;
        if (status != HttpStatus.SC_CREATED) {
            inputStream = conn.getErrorStream();
            Assert.fail("Error code was not expected (" + status + ")");
        } else {
            inputStream = conn.getInputStream();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
            inputStream, StandardCharsets.UTF_8));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
        }

        reader.close();

        Object response = JsonManager.deserializeJsonFromString(sb.toString(), klass);
        return response;
    }
}
