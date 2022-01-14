package com.apriori.fms.controller;

import com.apriori.fms.entity.response.FileResponse;
import com.apriori.fms.entity.response.FilesResponse;
import com.apriori.fms.enums.FMSAPIEnum;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.applicationmetadata.ApplicationMetadataUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.util.HashMap;
import java.util.Map;

public class FileManagementController {

    public static ResponseWrapper<FilesResponse> getFiles(UserCredentials userCredentials) {
        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.GET_FILES, FilesResponse.class)
            .headers(initHeaders(userCredentials, false));

        return HTTPRequest.build(requestEntity).get();
    }

    public static ResponseWrapper<FileResponse> getFileByIdentity(UserCredentials userCredentials, String fileIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.GET_FILE_BY_ID, FileResponse.class)
            .headers(initHeaders(userCredentials, false))
            .inlineVariables(fileIdentity);

        return HTTPRequest.build(requestEntity).get();
    }

    public static ResponseWrapper<FileResponse> uploadFile(UserCredentials userCredentials, ProcessGroupEnum processGroup, String fileName) {
        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.POST_FILES, FileResponse.class)
            .headers(initHeaders(userCredentials, true))
            .multiPartFiles(new MultiPartFiles()
                        .use("data", FileResourceUtil.getCloudFile(processGroup, fileName))
            ).formParams(new FormParams()
                        .use("filename", fileName)
                        .use("folder", "QAAutomationFolder")
            );

        return HTTPRequest.build(requestEntity).postMultipart();
    }

    private static Map<String, String> initHeaders(UserCredentials userCredentials, boolean addMultiPartFile) {
        Map<String, String> headers = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + userCredentials.getToken());
                put("ap-cloud-context", new ApplicationMetadataUtil().getAuthTargetCloudContext(userCredentials));
            }};

        if (addMultiPartFile) {
            headers.put("Content-Type", "multipart/form-data");
        }
        return headers;
    }

}
