package com.apriori.fms.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class FileManagementControllerTest extends TestUtil {

    private static UserCredentials userCredentials;

    @BeforeClass
    public static void getAuthorizationToken() {
        userCredentials = UserUtil.getUserWithCloudContext();
    }

    @Test
    @TestRail(id = 3933)
    @Description("Get files for a targetCloudContext with an authorized user")
    public void getFiles() {
        List<FileResponse> files = FileManagementController.getFiles(userCredentials).getResponseEntity().getResponse().getItems();

        String fileIdentity = files.get(new Random().nextInt(files.size())).getIdentity();

        assertThat(FileManagementController.getFileByIdentity(userCredentials, fileIdentity).getResponseEntity().getIdentity(), is(notNullValue()));
    }

    @Test
    @TestRail(id = {3939, 3934})
    @Description("Upload a file for a targetCloudContext with an authorized user")
    public void uploadFile() {
        String fileIdentity = FileManagementController.uploadFile(userCredentials, ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt").getIdentity();

        assertThat(FileManagementController.getFileByIdentity(userCredentials, fileIdentity).getResponseEntity().getFilename(), is(equalTo("bracket_basic.prt")));
    }
}
