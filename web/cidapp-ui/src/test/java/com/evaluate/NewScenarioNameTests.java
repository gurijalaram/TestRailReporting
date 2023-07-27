package com.evaluate;

import static com.apriori.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.navtoolbars.ScenarioPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class NewScenarioNameTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioPage scenarioPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemD;
    private SoftAssertions softAssertions = new SoftAssertions();

    public NewScenarioNameTests() {
        super();
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(id = {5424})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String testScenarioName = generateStringUtil.generateScenarioName();
        String testScenarioName2 = generateStringUtil.generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .copyScenario()
            .enterScenarioName(testScenarioName2)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isCurrentScenarioNameDisplayed(testScenarioName2), is(true));
    }

    @Test
    @TestRail(id = {5953})
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "MultiUpload";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioA = generateStringUtil.generateScenarioName();
        String scenarioB = generateStringUtil.generateScenarioName();
        String scenarioC = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItemB = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioA, resourceFile, currentUser);

        cidComponentItemC = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .uploadComponent(componentName, scenarioB, resourceFile, currentUser);

        cidComponentItemD = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, EvaluatePage.class)
            .uploadComponent(componentName, scenarioC, resourceFile, currentUser);

        explorePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemD, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "MultiUpload")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioA)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioB)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioC)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5425})
    @Description("Failure to create a new scenario that is named identical to existing scenario")
    public void usingAnExistingScenarioName() {

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(PLASTIC_MOLDING, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        scenarioPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .copyScenario()
            .enterScenarioName(scenarioName)
            .submit(ScenarioPage.class);

        assertThat(scenarioPage.getErrorMessage(), is("Must be unique."));
    }
}