package com.evaluate;

import static com.apriori.utils.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class DeleteTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private DeletePage deletePage;
    private File resourceFile;
    private File resourceFile2;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItem2;

    public DeleteTests() {
        super();
    }

    @Test
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .delete()
            .submit(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .delete()
            .submit(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(testCaseId = {"5432", "6730"})
    @Description("Test a private scenario can be deleted from the evaluate view")
    public void testDeletePrivateScenarioEvaluate() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .delete()
            .submit(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(testCaseId = {"13306"})
    @Description("Test a public scenario can be deleted from the evaluate view")
    public void testDeletePublicScenarioEvaluate() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .delete()
            .submit(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test group delete")
    public void testGroupDelete() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItem2 = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        deletePage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .delete();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(deletePage.getScenarioNames()).contains(componentName.toUpperCase() + "  / " + scenarioName, componentName2 + "  / " + scenarioName2);

        explorePage = deletePage.submit(DeletePage.class)
            .close(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .checkComponentDelete(cidComponentItem2)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioName)).isEqualTo(0);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName2)).isEqualTo(0);

        softAssertions.assertAll();
    }
}