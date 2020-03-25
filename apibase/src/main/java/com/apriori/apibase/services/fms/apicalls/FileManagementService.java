package com.apriori.apibase.services.fms.apicalls;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.http.builder.service.RequestAreaCds;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.fms.objects.FilesResponse;
import com.apriori.apibase.services.fms.objects.UploadResponse;
import com.apriori.apibase.services.objects.Token;
import com.apriori.apibase.utils.FormParams;
import com.apriori.apibase.utils.MultiPartFiles;
import com.apriori.apibase.utils.ResponseWrapper;
import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;

import java.io.File;
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

        return  GenericRequestUtil.get(requestEntity, new RequestAreaCds());
    }


    public static ResponseWrapper<UploadResponse> uploadFile(String token, String fileName) {

        RequestEntity requestEntity = RequestEntity.init(String.format(finalUrl, Constants.getFmsServiceHost()), UploadResponse.class)
                .setHeaders(initHeaders(token, true))
                .setMultiPartFiles(new MultiPartFiles()
                        .use("data", Util.getLocalResourceFile(fileName))
                )
                .setFormParams(new FormParams()
                        .use("filename", fileName)
                        .use("folder", "QAAutomationFolder")
                );

        return GenericRequestUtil.postMultipart(requestEntity, new RequestAreaCds());
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
