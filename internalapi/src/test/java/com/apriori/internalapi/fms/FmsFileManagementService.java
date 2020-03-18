package com.apriori.internalapi.fms;

import com.apriori.apibase.services.fms.apicalls.FileManagementService;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.fms.objects.FilesResponse;
import com.apriori.apibase.services.fms.objects.UploadResponse;
import com.apriori.apibase.services.utils.SecurityManager;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class FmsFileManagementService {

    private static String token;

    @BeforeClass
    public static void getAuthorizationToken() {
        token = SecurityManager.retriveJwtToken(
            Constants.getAtsServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getAtsTokenUsername(),
            Constants.getAtsTokenEmail(),
            Constants.getAtsTokenIssuer(),
            Constants.getAtsTokenSubject());
    }

    @Test
    @TestRail(testCaseId = "3933")
    @Description("Get files for a targetCloudContext with an authorized user")
    public void getFiles() {
        FilesResponse response = FileManagementService.getFiles(
                Constants.getFmsServiceHost(),
                HttpStatus.SC_OK,
                token,
                Constants.getAtsAuthTargetCloudContext());
    }

    @Test
    @TestRail(testCaseId = "3934")
    @Description("Get file by identity for a targetCloudContext with an authorized user")
    public void getFileByIdentity() {
        FileResponse response = FileManagementService.getFileByIdentity(
                Constants.getFmsServiceHost(),
                HttpStatus.SC_OK,
                token,
                Constants.getAtsAuthTargetCloudContext(),
                Constants.getFmsFileIdentity());
    }


    @Test
    @TestRail(testCaseId = "3939")
    @Description("Upload a file for a targetCloudContext with an authorized user")
    public void  upLoadFile() {
        UploadResponse response = FileManagementService.uploadFile(
                Constants.getFmsServiceHost(),
                HttpStatus.SC_CREATED,
                token,
                Constants.getAtsAuthTargetCloudContext(),
                "bracket_basic.prt"
        );
    }
}