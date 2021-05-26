package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class UploadComponentTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluateToolbar evaluateToolbar;

    @Test
    @Category(SmokeTests.class)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"Casting.prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, ExplorePage.class)
            .inputFilter("Recent");

        assertThat(explorePage.getListOfScenarios("CASTING", scenarioName), Matchers.is(equalTo(1)));
    }
}
