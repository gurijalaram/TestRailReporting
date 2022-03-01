package com.explore;

import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiUploadTests extends TestBase {

    private File resourceFile;
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ImportCadFilePage importCadFilePage;

    @Test
    @Description()
    public void multiupload() {
        currentUser = UserUtil.getUser();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .multiUploadComponent(multiComponents);
    }
}
