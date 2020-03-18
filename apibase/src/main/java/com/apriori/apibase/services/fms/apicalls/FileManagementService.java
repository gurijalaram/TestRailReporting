package com.apriori.apibase.services.fms.apicalls;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.fms.objects.FilesResponse;
import com.apriori.apibase.services.fms.objects.UploadResponse;
import com.apriori.apibase.services.objects.Token;
import com.apriori.apibase.utils.FormParams;
import com.apriori.apibase.utils.MultiPartFiles;
import com.apriori.utils.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileManagementService {
    public static FilesResponse getFiles(String url, int statusCode, String token, String targetCloudContext) {
        url = "https://" + url + "/files";
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", targetCloudContext);
            }};
        return (FilesResponse) ServiceConnector.getService(url, FilesResponse.class, statusCode, headers);
    }

    public static FileResponse getFileByIdentity(String url, int statusCode, String token, String targetCloudContext, String fileIdentity) {
        url = "https://" + url + "/files/" +  fileIdentity;
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", targetCloudContext);
            }};
        return (FileResponse) ServiceConnector.getService(url, FileResponse.class, statusCode, headers);
    }

    public static UploadResponse uploadFile(String url, int statusCode, String token, String targetCloudContext, String fileName) {
        url = "https://" + url + "/files";
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", targetCloudContext);
                put("Content-Type", "multipart/form-data");
            }};

        final File testData = Util.getLocalResourceFile(fileName);
        MultiPartFiles multiPartFiles = new MultiPartFiles().use("data", testData);

        FormParams formParams = new FormParams();
        formParams.use("filename", fileName);
        formParams.use("folder", "QAAutomationFolder");

        return (UploadResponse) ServiceConnector.postToService(url, UploadResponse.class, statusCode, headers, multiPartFiles, formParams);
    }

}
