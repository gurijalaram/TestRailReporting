package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.ScenarioPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class NewScenarioNameTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioPage scenarioPage;
    private EvaluatePage evaluatePage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    public NewScenarioNameTests() {
        super();
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {5424})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {
        String testScenarioName2 = generateStringUtil.generateScenarioName();
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .copyScenario()
            .enterScenarioName(testScenarioName2)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isCurrentScenarioNameDisplayed(testScenarioName2), is(true));
    }

    @Test
    @TestRail(id = {5953})
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {
        String filterName = generateStringUtil.generateFilterName();

        ComponentInfoBuilder component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);
        componentB.setUser(component.getUser());
        ComponentInfoBuilder componentC = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);
        componentC.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .selectProcessGroup(componentB.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(componentB, EvaluatePage.class)
            .uploadComponentAndOpen(componentC)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(componentC, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "MultiUpload")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentC.getComponentName(), componentC.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5425})
    @Description("Failure to create a new scenario that is named identical to existing scenario")
    public void usingAnExistingScenarioName() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        scenarioPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .copyScenario()
            .enterScenarioName(component.getScenarioName())
            .submit(ScenarioPage.class);

        assertThat(scenarioPage.getErrorMessage(), is("Must be unique."));
    }
}