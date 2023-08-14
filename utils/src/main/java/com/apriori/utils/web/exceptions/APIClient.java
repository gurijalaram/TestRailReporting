/**
 * TestRail API binding for Java (API v2, available since TestRail 3.0)
 * Updated for TestRail 5.7
 * <p>
 * Learn more:
 * <p>
 * http://docs.gurock.com/testrail-api2/start
 * http://docs.gurock.com/testrail-api2/accessing
 * <p>
 * Copyright Gurock Software GmbH. See license.md for details.
 */

package com.apriori.utils.web.exceptions;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class APIClient {
    private String myUser;
    private String myPassword;
    private String myUrl;

    public APIClient(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        this.myUrl = baseUrl + "index.php?/api/v2/";
    }

    /**
     * Get/Set User
     * <p>
     * Returns/sets the user used for authenticating the API requests.
     */
    public String getUser() {
        return this.myUser;
    }

    public void setUser(String user) {
        this.myUser = user;
    }

    /**
     * Get/Set Password
     * <p>
     * Returns/sets the password used for authenticating the API requests.
     */
    public String getPassword() {
        return this.myPassword;
    }

    public void setPassword(String password) {
        this.myPassword = password;
    }

    /**
     * Send Get
     * <p>
     * Issues a GET request (read) against the API and returns the result (as
     * Object, see below).
     * <p>
     * Arguments:
     * <p>
     * uri The API method to call including parameters (e.g. get_case/1)
     * <p>
     * Returns the parsed JSON response as standard object which can either be
     * an instance of JSONObject or JSONArray (depending on the API method). In
     * most cases, this returns a JSONObject instance which is basically the
     * same as java.util.Map.
     * <p>
     * If 'get_attachment/:attachment_id', returns a String
     */
    public Object sendGet(String uri, String data)
        throws IOException, APIException {
        return this.sendRequest("GET", uri, data);
    }

    public Object sendGet(String uri) throws IOException, APIException {
        return this.sendRequest("GET", uri, null);
    }

    /**
     * Send POST
     * <p>
     * Issues a POST request (write) against the API and returns the result (as
     * Object, see below).
     * <p>
     * Arguments:
     * <p>
     * uri The API method to call including parameters (e.g. add_case/1) data
     * The data to submit as part of the request (e.g., a map) If adding an
     * attachment, must be the path to the file
     * <p>
     * Returns the parsed JSON response as standard object which can either be
     * an instance of JSONObject or JSONArray (depending on the API method). In
     * most cases, this returns a JSONObject instance which is basically the
     * same as java.util.Map.
     */
    public Object sendPost(String uri, Object data)
        throws IOException, APIException {
        return this.sendRequest("POST", uri, data);
    }

    private Object sendRequest(String method, String uri, Object data)
        throws IOException, APIException {
        URL url = new URL(this.myUrl + uri);
        // Create the connection object and set the required HTTP method
        // (GET/POST) and headers (content type and basic auth).
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String auth = getAuthorization(this.myUser, this.myPassword);
        conn.addRequestProperty("Authorization", "Basic " + auth);

        if (method.equals("POST")) {
            conn.setRequestMethod("POST");
            // Add the POST arguments, if any. We just serialize the passed
            // data object (i.e. a dictionary) and then add it to the
            // request body.
            if (data != null) {
                if (uri.startsWith("add_attachment")) {  // add_attachment API requests
                    String boundary = "TestRailAPIAttachmentBoundary"; // Can be
                    // any
                    // random
                    // string
                    File uploadFile = new File((String) data);

                    conn.setDoOutput(true);
                    conn.addRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);

                    OutputStream ostreamBody = conn.getOutputStream();
                    BufferedWriter bodyWriter = new BufferedWriter(
                        new OutputStreamWriter(ostreamBody));

                    bodyWriter.write("--" + boundary + "\r");
                    bodyWriter
                        .write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
                            + uploadFile.getName() + "\"");
                    bodyWriter.write("\r\r");
                    bodyWriter.flush();

                    // Read file into request
                    InputStream istreamFile = new FileInputStream(uploadFile);
                    int bytesRead;
                    byte[] dataBuffer = new byte[1024];
                    while ((bytesRead = istreamFile.read(dataBuffer)) != -1) {
                        ostreamBody.write(dataBuffer, 0, bytesRead);
                    }

                    ostreamBody.flush();

                    // end of attachment, add boundary
                    bodyWriter.write("\r--" + boundary + "--\r");
                    bodyWriter.flush();

                    // Close streams
                    istreamFile.close();
                    ostreamBody.close();
                    bodyWriter.close();
                } else { // Not an attachment
                    conn.addRequestProperty("Content-Type", "application/json");
                    byte[] block = JSONValue.toJSONString(data).getBytes(
                        StandardCharsets.UTF_8);

                    conn.setDoOutput(true);
                    OutputStream ostream = conn.getOutputStream();
                    ostream.write(block);
                    ostream.close();
                }
            }
        } else { // GET request
            conn.addRequestProperty("Content-Type", "application/json");
        }

        // Execute the actual web request (if it wasn't already initiated
        // by getOutputStream above) and record any occurred errors (we use
        // the error stream in this case).
        int status = conn.getResponseCode();

        InputStream istream;
        if (status != HttpStatus.SC_OK) {
            istream = conn.getErrorStream();
            if (istream == null) {
                throw new APIException("TestRail API return HTTP " + status
                    + " (No additional error message received)");
            }
        } else {
            istream = conn.getInputStream();
        }

        // If 'get_attachment/' returned valid status code, save the file
        if ((istream != null) && (uri.startsWith("get_attachment/"))) {
            FileOutputStream outputStream = new FileOutputStream((String) data);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = istream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            istream.close();
            return data;
        }

        // Not an attachment received
        // Read the response body, if any, and deserialize it from JSON.
        StringBuilder text = new StringBuilder();
        if (istream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                istream, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append(System.getProperty("line.separator"));
            }

            reader.close();
        }

        Object result;
        if (!text.toString().equals("")) {
            result = JSONValue.parse(text.toString());
        } else {
            result = new JSONObject();
        }

        // Check for any occurred errors and add additional details to
        // the exception message, if any (e.g. the error message returned
        // by TestRail).
        if (status != HttpStatus.SC_OK) {
            String error = "No additional error message received";
            if (result instanceof JSONObject) {
                JSONObject obj = (JSONObject) result;
                if (obj.containsKey("error")) {
                    error = '"' + (String) obj.get("error") + '"';
                }
            }

            throw new APIException("TestRail API returned HTTP " + status + "("
                + error + ")");
        }

        return result;
    }

    private static String getAuthorization(String user, String password) {
        try {
            return new String(Base64.getEncoder().encode(
                (user + ":" + password).getBytes()));
        } catch (IllegalArgumentException e) {
            // Not thrown
        }

        return "";
    }
}
