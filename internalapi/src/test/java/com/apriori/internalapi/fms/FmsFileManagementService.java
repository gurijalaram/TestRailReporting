package com.apriori.internalapi.fms;

import com.apriori.apibase.services.fms.apicalls.FileManagementService;
import com.apriori.apibase.services.utils.SecurityManager;
import com.apriori.internalapi.util.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class FmsFileManagementService extends TestUtil {

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
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                FileManagementService.getFiles(
                        token
                ).getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "3934")
    @Description("Get file by identity for a targetCloudContext with an authorized user")
    public void getFileByIdentity() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                FileManagementService.getFileByIdentity(
                        token,
                        Constants.getFmsFileIdentity()
                ).getStatusCode());
    }


    @Test
    @TestRail(testCaseId = "3939")
    @Description("Upload a file for a targetCloudContext with an authorized user")
    public void upLoadFile() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
                FileManagementService.uploadFile(
                        token,
                        "bracket_basic.prt"
                ).getStatusCode());
    }
}