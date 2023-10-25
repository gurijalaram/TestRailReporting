package com.apriori;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Random;

@ExtendWith(TestRulesAPI.class)
public class FileManagementControllerTest extends TestUtil {

    private static UserCredentials userCredentials;

    @BeforeAll
    public static void getAuthorizationToken() {
        userCredentials = UserUtil.getUserWithCloudContext();
    }

    @Test
    @Tag(API_SANITY)
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
