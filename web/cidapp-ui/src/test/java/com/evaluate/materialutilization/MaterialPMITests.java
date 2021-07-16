package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class MaterialPMITests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    private File resourceFile;


    public MaterialPMITests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @TestRail(testCaseId = {"6283"})
    @Description("Test setting a default material and ensure parts are costed in that material by default")
    public void materialTestProductionDefault() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            /*.openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterial("Aluminum, Stock, ANSI 6061");
        new SettingsPage(driver).save(ExplorePage.class);*/

            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario(3);

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Stock, ANSI 6061"), is(true));
    }
}
