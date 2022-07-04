package com.partsandassembliesdetails;

import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


public class PartsAndAssembliesDetailsTest extends TestBase {

    public PartsAndAssembliesDetailsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;


    @Test
    @TestRail(testCaseId = {"12396","12458","12460","12461"})
    @Description("Verify 3D viewer and column cards on parts and assemblies details page")
    public void testPartsAndAssembliesDetailPageHeaderTitle() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnScenarioName(scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderText()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerToolBarDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioResultsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInsightsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentsCardDisplayed()).isEqualTo(true);

        softAssertions.assertAll();

    }

}


