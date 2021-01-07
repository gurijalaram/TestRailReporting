package com.apriori.fms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.service.SecurityManager;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.fms.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileManagementControllerTest extends TestUtil {

    private static String token;

    @BeforeClass
    public static void getAuthorizationToken() {
        token = SecurityManager.retrieveJwtToken(
            Constants.getServiceHost(),
            HttpStatus.SC_CREATED,
            "splunkett",
            "splunkett@apriori.com",
            Constants.getFmsTokenIssuer(),
            Constants.getFmsTokenSubject());
    }

    @Test
    @TestRail(testCaseId = "3933")
    @Description("Get files for a targetCloudContext with an authorized user")
    public void getFiles() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            FileManagementController.getFiles(
                token
            ).getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "3934")
    @Description("Get file by identity for a targetCloudContext with an authorized user")
    public void getFileByIdentity() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            FileManagementController.getFileByIdentity(
                token,
                Constants.getFmsFileIdentity()
            ).getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "3939")
    @Description("Upload a file for a targetCloudContext with an authorized user")
    public void upLoadFile() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
            FileManagementController.uploadFile(
                token,
                ProcessGroupEnum.SHEET_METAL,
                "bracket_basic.prt"
            ).getStatusCode());
    }
}