package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishTests extends TestBase {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;

    public PublishTests() {
        super();
    }

    @Test
    @Category({SmokeTests.class, SanityTests.class})
    @Description("Publish a new scenario from the Private Workspace to the Public Workspace")
    @TestRail(testCaseId = {"6729", "6731"})
    public void testPublishNewCostedScenario() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = {"6743", "6744", "6745", "6747"})
    @Description("Publish a part and add an assignee, cost maturity and status")
    public void testPublishWithStatus() {

        final String file = "testpart-4.prt";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .selectStatus("Analysis")
            .selectCostMaturity("Low")
            .selectAssignee("Abe Chaves")
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Scenario Name", "Contains", scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(greaterThan(0)));
    }
}