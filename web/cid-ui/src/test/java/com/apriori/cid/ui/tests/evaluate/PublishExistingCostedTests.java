package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
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

public class PublishExistingCostedTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder component;
    private SoftAssertions softAssertions = new SoftAssertions();

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6209, 5427, 6732})
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {
        String filterName = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .costScenario()
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, component.getComponentName())
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isGreaterThan(0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6211, 6734, 6040})
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);
        componentB.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component).
            selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .uploadComponentAndOpen(componentB).clickExplore()
            .selectFilter("Private")
            .enterKeySearch(component.getComponentName())
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component.getComponentName(), component.getScenarioName())
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Material Stock / Band Saw / Preheat / Hammer / Trim");

        softAssertions.assertAll();
    }
}