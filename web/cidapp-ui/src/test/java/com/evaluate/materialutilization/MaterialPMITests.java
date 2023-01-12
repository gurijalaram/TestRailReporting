package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

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
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6283", "5917"})
    @Description("Test setting a default material and ensure parts are costed in that material by default")
    public void materialTestProductionDefault() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_BRAZIL)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_6061.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario(3);

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_6061.getMaterialName()), is(true));
    }
}
