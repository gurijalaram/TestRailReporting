package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
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
import org.junit.jupiter.api.Test;

public class DeleteTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentInfoBuilder component;

    public DeleteTests() {
        super();
    }

    @Test
    @TestRail(id = {6736, 5431})
    @Description("Test a private scenario can be deleted from the component table")
    public void testDeletePrivateScenario() {
        String filterName = new GenerateStringUtil().generateFilterName();

        component = new ComponentRequestUtil().getWithoutPgComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, component.getScenarioName())
            .submit(ExplorePage.class)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(component)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {7709})
    @Description("Test a public scenario can be deleted from the component table")
    public void testDeletePublicScenario() {
        String filterName = new GenerateStringUtil().generateFilterName();

        component = new ComponentRequestUtil().getWithoutPgComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, component.getScenarioName())
            .submit(ExplorePage.class)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(component)
            .refresh();

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {5432, 6730})
    @Description("Test a private scenario can be deleted from the evaluate view")
    public void testDeletePrivateScenarioEvaluate() {
        String filterName = new GenerateStringUtil().generateFilterName();

        component = new ComponentRequestUtil().getWithoutPgComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(component)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, component.getScenarioName())
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {13306})
    @Description("Test a public scenario can be deleted from the evaluate view")
    public void testDeletePublicScenarioEvaluate() {
        String filterName = new GenerateStringUtil().generateFilterName();

        component = new ComponentRequestUtil().getWithoutPgComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(component)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, component.getScenarioName())
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(id = {6737, 6738})
    @Description("Test an edited private scenario and the original public scenario, which is locked, can be deleted from the evaluate view")
    public void testDeletePublicAndPrivateScenarios() {
        String filterName = new GenerateStringUtil().generateFilterName();

        component = new ComponentRequestUtil().getWithoutPgComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .lock()
            .publish(component, EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Public")
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(component)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, component.getScenarioName())
            .submit(ExplorePage.class);

        assertThat(explorePage.getScenarioMessage(), containsString("No scenarios found"));
    }
}