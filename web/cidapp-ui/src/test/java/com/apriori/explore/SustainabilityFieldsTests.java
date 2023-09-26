package com.apriori.explore;

import static com.utils.ColumnsEnum.LOGISTICS_CARBON;
import static com.utils.ColumnsEnum.MATERIAL_CARBON;
import static com.utils.ColumnsEnum.PROCESS_CARBON;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class SustainabilityFieldsTests extends TestBaseUI {
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private final SoftAssertions softAssertions = new SoftAssertions();
    private File resourceFile;
    private File resourceFile2;
    private ComparePage comparePage;

    @Test
    @TestRail(id = 24429)
    @Description("Verify sustainability fields In explore view")
    public void sustainabilityFieldsInExploreView() {
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .configure()
            .selectColumn(MATERIAL_CARBON)
            .selectColumn(PROCESS_CARBON)
            .selectColumn(LOGISTICS_CARBON)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        List<String> headers = explorePage.getTableHeaders();
        softAssertions.assertThat(headers).contains("Material Carbon","Process Carbon","Logistics Carbon");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 24101)
    @Description("Verify sustainability fields In comparison view")
    public void sustainabilityFieldsComparisonView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        comparePage.getSustainabilityHeaderDetails();
        softAssertions.assertThat(comparePage.getCardHeader()).contains("Sustainability");
        softAssertions.assertThat(comparePage.getSustainabilityHeaderDetails())
            .contains("Material Carbon","Process Carbon","Logistics Carbon","Total Carbon","Annual Manufacturing Carbon");
        softAssertions.assertAll();
    }
}
