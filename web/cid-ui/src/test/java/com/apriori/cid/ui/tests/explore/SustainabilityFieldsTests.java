package com.apriori.cid.ui.tests.explore;

import static com.apriori.cid.ui.utils.ColumnsEnum.LOGISTICS_CARBON;
import static com.apriori.cid.ui.utils.ColumnsEnum.MATERIAL_CARBON;
import static com.apriori.cid.ui.utils.ColumnsEnum.PROCESS_CARBON;

import com.apriori.cid.ui.pageobjects.compare.ComparePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.DirectionEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

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
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentA)
            .uploadComponentAndOpen(componentB)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .createComparison()
            .selectManualComparison();

        comparePage.getSustainabilityHeaderDetails();
        softAssertions.assertThat(comparePage.getCardHeader()).contains("Sustainability");
        softAssertions.assertThat(comparePage.getSustainabilityHeaderDetails())
            .contains("Material Carbon","Process Carbon","Logistics Carbon","Total Carbon","Annual Manufacturing Carbon");
        softAssertions.assertAll();
    }
}
