package com.apriori.apibase.services.fms.apicalls;

import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.fms.objects.FileResponses;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class FileManagementService {

    private static String finalUrl = "https://%s/files/";

    public static ResponseWrapper<FileResponses> getFiles(String serviceHost, String token, String cloudContext) {
        return getFileInfo(FileResponses.class, serviceHost, token, null, cloudContext);
    }

    public static ResponseWrapper<FileResponse> getFileByIdentity(String serviceHost, String token, String fileIdentity, String cloudContext) {
        return getFileInfo(FileResponse.class, serviceHost, token, fileIdentity, cloudContext);
    }

    public static <T> ResponseWrapper<T> getFileInfo(Class klass, String url, String token, String fileIdentity, String cloudContext) {
        String requestUrl = String.format(finalUrl, url);

        if (fileIdentity != null) {
            requestUrl = requestUrl.concat(fileIdentity);
        }

        RequestEntity requestEntity = RequestEntity.init(requestUrl, klass)
            .setHeaders(initHeaders(token, false, cloudContext));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    public static ResponseWrapper<FileResponse> uploadFile(String serviceHost, String token, String fileName, String cloudContext) {

        RequestEntity requestEntity = RequestEntity.init(String.format(finalUrl, serviceHost), FileResponse.class)
            .setHeaders(initHeaders(token, true, cloudContext))
            .setMultiPartFiles(new MultiPartFiles()
                .use("data", FileResourceUtil.getResourceAsFile(fileName))
            )
            .setFormParams(new FormParams()
                .use("filename", fileName)
                .use("folder", "QAAutomationFolder")
            );

        return GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi());
    }

    private static Map<String, String> initHeaders(String token, boolean addMultiPartFile, String cloudContext) {
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", cloudContext);
            }};

        if (addMultiPartFile) {
            headers.put("Content-Type", "multipart/form-data");
        }
        return headers;
    }

}
