package com.apriori.fms.controller;

import com.apriori.fms.entity.response.FileResponse;
import com.apriori.fms.entity.response.FilesResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import java.util.HashMap;
import java.util.Map;

public class FileManagementController {

    private static String finalUrl = PropertiesContext.get("${env}.fms.api_url") + "files/";

    public static ResponseWrapper<FilesResponse> getFiles(String token) {
        return getFileInfo(FilesResponse.class, token, null);
    }

    public static ResponseWrapper<FileResponse> getFileByIdentity(String token, String fileIdentity) {
        return getFileInfo(FileResponse.class, token, fileIdentity);
    }

    public static <T> ResponseWrapper<T> getFileInfo(Class klass, String token,  String fileIdentity) {
        String requestUrl = finalUrl;

        if (fileIdentity != null) {
            requestUrl = requestUrl.concat(fileIdentity);
        }

        RequestEntity requestEntity = RequestEntity.init(requestUrl, klass)
                .setHeaders(initHeaders(token, false));

        return  GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    public static ResponseWrapper<FileResponse> uploadFile(String token, ProcessGroupEnum processGroup, String fileName) {

        RequestEntity requestEntity = RequestEntity.init(finalUrl, FileResponse.class)
                .setHeaders(initHeaders(token, true))
                .setMultiPartFiles(new MultiPartFiles()
                        .use("data", FileResourceUtil.getCloudFile(processGroup, fileName))
                )
                .setFormParams(new FormParams()
                        .use("filename", fileName)
                        .use("folder", "QAAutomationFolder")
                );

        return GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi());
    }

    private static Map<String, String> initHeaders(String token, boolean addMultiPartFile) {
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", PropertiesContext.get("${env}.ats.auth_target_cloud_context"));
            }};

        if (addMultiPartFile) {
            headers.put("Content-Type", "multipart/form-data");
        }
        return headers;
    }

}
