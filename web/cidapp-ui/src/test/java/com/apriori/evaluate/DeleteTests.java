package com.apriori.evaluate;

import static com.apriori.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DeleteTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;

    public DeleteTests() {
        super();
    }

    @Test
    @TestRail(id = {6736, 5431})
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
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {7709})
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
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
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
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {5432, 6730})
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
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {13306})
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
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {6737, 6738})
    @Description("Test an edited private scenario and the original public scenario, which is locked, can be deleted from the evaluate view")
    public void testDeletePublicAndPrivateScenarios() {
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
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .lock()
            .publish(cidComponentItem, EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Public")
            .openScenario(componentName, scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }
}