package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiUploadTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    @Test
    @TestRail(testCaseId = "CIS-304")
    @Description("Validate multi-upload through explorer menu")
    public void multiUploadTests() {
        currentUser = UserUtil.getUser();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentDetails(multiComponents)
            .submit()
            .close();

        multiComponents.forEach(component -> assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0], component.getScenarioName()), is(equalTo(1))));
    }
}
