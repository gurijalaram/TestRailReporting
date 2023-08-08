package com.apriori.fms.controller;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.enums.FMSAPIEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.fms.models.response.FilesResponse;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileManagementController {

    /**
     * Gets all files
     *
     * @param userCredentials - user to authenticate with
     * @return ResponseWrapper of type FilesResponse instance (api response)
     */
    public static ResponseWrapper<FilesResponse> getFiles(UserCredentials userCredentials) {
        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.FILES, FilesResponse.class)
            .headers(initHeaders(userCredentials, false))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets a particular file
     *
     * @param userCredentials - user to authenticate with
     * @param fileIdentity    - file to retrieve
     * @return ResponseWrapper of type FilesResponse instance (api response)
     */
    public static ResponseWrapper<FileResponse> getFileByIdentity(UserCredentials userCredentials, String fileIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.FILE_BY_ID, FileResponse.class)
            .headers(initHeaders(userCredentials, false))
            .inlineVariables(fileIdentity);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Upload a file
     *
     * @param userCredentials - user to authenticate with
     * @param processGroup    - process group to use to find the part on S3
     * @param fileName        - name of the file to find it on S3
     * @return FileResponse instance
     */
    public static FileResponse uploadFile(UserCredentials userCredentials, ProcessGroupEnum processGroup, String fileName) {
        return uploadFileWithResourceName(userCredentials, processGroup, fileName, null);
    }

    /**
     * Upload a file
     *
     * @param userCredentials - user to authenticate with
     * @param processGroup    - process group to use to find the part on S3
     * @param fileName        - name of the file to find it on S3
     * @return FileResponse instance
     */
    public static FileResponse uploadFileWithResourceName(UserCredentials userCredentials, ProcessGroupEnum processGroup, String fileName, String resourceName) {
        return uploadFileWithResourceName(userCredentials, FileResourceUtil.getCloudFile(processGroup, fileName), resourceName);
    }

    /**
     * Upload a file
     *
     * @param userCredentials - user to authenticate with
     * @param fileToUpload    - file that should be uploaded
     * @param resourceName    - resource name received after cad file upload
     * @return FileResponse instance
     */
    public static FileResponse uploadFileWithResourceName(UserCredentials userCredentials, File fileToUpload, String resourceName) {
        QueryParams requestQueryParams = new QueryParams().use("filename", fileToUpload.getName());
        if (requestQueryParams != null) {
            requestQueryParams.use("resourceName", resourceName);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(FMSAPIEnum.FILES, FileResponse.class)
            .headers(initHeaders(userCredentials, true))
            .multiPartFiles(new MultiPartFiles().use("data", fileToUpload))
            .queryParams(requestQueryParams)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (FileResponse) HTTPRequest.build(requestEntity).postMultipart().getResponseEntity();
    }

    /**
     * Initializes headers (for both file upload and get file(s))
     *
     * @param userCredentials  - user to authenticate with
     * @param addMultiPartFile - boolean flag to determine if content type needs set
     * @return Map of String, String, to set as headers in api request
     */
    private static Map<String, String> initHeaders(UserCredentials userCredentials, boolean addMultiPartFile) {
        Map<String, String> headers = new HashMap<String, String>() {{
                put("ap-cloud-context", new AuthorizationUtil().getAuthTargetCloudContext(userCredentials));
                put("ap-user-context", new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()));
            }};

        if (addMultiPartFile) {
            headers.put("Content-Type", "multipart/form-data");
        }
        return headers;
    }
}
