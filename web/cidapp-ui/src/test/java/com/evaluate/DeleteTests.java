package com.evaluate;

import static com.apriori.utils.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.css.entity.response.Item;
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

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;

import java.io.File;

public class DeleteTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private UserCredentials currentUser;
    private Item cssItem;

    public DeleteTests() {
        super();
    }

    @Test
    @Ignore("Processing state")
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6736", "5431"})
    @Description("Test a private scenario can be deleted from the component table")
    public void testDeletePrivateScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Scenario Name", "Contains", scenarioName)
            .submit(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .delete()
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @Ignore("ProcessingState")
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"7709"})
    @Description("Test a public scenario can be deleted from the component table")
    public void testDeletePublicScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser,EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Scenario Name", "Contains", scenarioName)
            .submit(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .delete()
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }
}