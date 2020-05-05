package com.apriori.apibase.services.fms.apicalls;

import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.fms.objects.FilesResponse;
import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;
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

    public static ResponseWrapper<FilesResponse> getFiles(String token) {
        return getFileInfo(FilesResponse.class,  Constants.getFmsServiceHost(), token, null);
    }

    public static ResponseWrapper<FileResponse> getFileByIdentity(String token, String fileIdentity) {
        return getFileInfo(FileResponse.class,  Constants.getFmsServiceHost(), token, fileIdentity);
    }

    public static <T> ResponseWrapper<T> getFileInfo(Class klass, String url, String token,  String fileIdentity) {
        String requestUrl = String.format(finalUrl, url);

        if (fileIdentity != null) {
            requestUrl = requestUrl.concat(fileIdentity);
        }

        RequestEntity requestEntity = RequestEntity.init(requestUrl, klass)
                .setHeaders(initHeaders(token, false));

        return  GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }


    public static ResponseWrapper<FileResponse> uploadFile(String token, String fileName) {

        RequestEntity requestEntity = RequestEntity.init(String.format(finalUrl, Constants.getFmsServiceHost()), FileResponse.class)
                .setHeaders(initHeaders(token, true))
                .setMultiPartFiles(new MultiPartFiles()
                        .use("data", Util.getResourceAsFile(fileName))
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
                put("ap-cloud-context", Constants.getAtsAuthTargetCloudContext());
            }};

        if (addMultiPartFile) {
            headers.put("Content-Type", "multipart/form-data");
        }
        return headers;
    }

}
