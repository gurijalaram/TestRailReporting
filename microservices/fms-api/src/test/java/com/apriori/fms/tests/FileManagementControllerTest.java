package com.apriori.fms.tests;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.fms.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class FileManagementControllerTest extends TestUtil {

    private static String token;

    @BeforeClass
    public static void getAuthorizationToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"3933"})
    @Description("Get files for a targetCloudContext with an authorized user")
    public void getFiles() {
        List<FileResponse> files = FileManagementController.getFiles(
            token
        ).getResponseEntity().getResponse().getItems();

        String fileIdentity = files.get(new Random().nextInt(files.size())).getIdentity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                FileManagementController.getFileByIdentity(
                        token,
                        fileIdentity
                ).getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"3939", "3934"})
    @Description("Upload a file for a targetCloudContext with an authorized user")
    public void upLoadFile() {
        String fileIdentity = FileManagementController.uploadFile(
            token,
            ProcessGroupEnum.SHEET_METAL,
            "bracket_basic.prt"
        ).getResponseEntity().getResponse().getIdentity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                FileManagementController.getFileByIdentity(
                        token,
                        fileIdentity
                ).getStatusCode());
    }
}
