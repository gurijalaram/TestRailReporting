package com.explore;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.GenericHeader;
import com.pageobjects.toolbars.PageHeader;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PageHeaderTests extends TestBase {
    private CidLoginPage loginPage;
    private GenericHeader genericHeader;
    private PageHeader pageHeader;

    private File resourceFile;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"449"})
    @Description("Tooltips should be displayed to user upon hover over of Evaluate Tab & Compare Tab showing their current state")
    public void tooltipTestEvaluateAndCompareTab() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String evaluateTooltipText = "There is no active scenario.\n- Select one in Explore\nOR\n- Use New to create a new one.";
        String compareTooltipText = "There is no active comparison.\n- Select one in Explore\nOR\n- Use New to create a new one.";

        loginPage = new CidLoginPage(driver);
        pageHeader = loginPage.login(UserUtil.getUser())
                .hoverOnEvaluateTab();

        assertThat(pageHeader.getTitleEvaluateTab(), is(evaluateTooltipText));

        pageHeader.hoverOnCompareTab();

        assertThat(pageHeader.getTitleCompareTab(), is(compareTooltipText));

        genericHeader = new GenericHeader(driver);
        pageHeader = genericHeader.uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .selectExploreButton()
                .hoverOnEvaluateTab();

        assertThat(pageHeader.getTitleEvaluateTab(), is("Part  " + partName.toUpperCase() + " ,  " + "Scenario  " + testScenarioName + " "));

        genericHeader = new GenericHeader(driver);
        pageHeader = genericHeader.createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(testScenarioName, partName)
                .apply(GenericHeader.class)
                .selectExploreButton()
                .hoverOnCompareTab();

        assertThat(pageHeader.getTitleCompareTab(), is("Comparison  " + testComparisonName.toUpperCase()));
    }
}
