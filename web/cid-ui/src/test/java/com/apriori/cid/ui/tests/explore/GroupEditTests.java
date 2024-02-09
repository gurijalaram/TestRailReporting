package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.EditComponentsPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class GroupEditTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EditComponentsPage editComponentsPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    @Test
    @TestRail(id = {14723})
    @Description("Verify user can edit multiple scenarios")
    public void testGroupEdit() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());
        ComponentInfoBuilder componentC = new ComponentRequestUtil().getComponent();
        componentC.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);

        editComponentsPage = loginPage.login(componentA.getUser())
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentC)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName(),
                componentC.getComponentName() + ", " + componentB.getScenarioName())
            .editScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("If you wish to retain existing private scenarios, change the scenario name, otherwise they will be overridden.");

        explorePage = editComponentsPage.overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentA.getComponentName(), componentA.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(1);

        explorePage.selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName(),
                componentC.getComponentName() + ", " + componentB.getScenarioName())
            .editScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(scenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentA.getComponentName(), scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentC.getComponentName(), scenarioName)).isEqualTo(1);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {14724})
    @Description("Attempt to edit multiple scenarios, including a private scenario")
    public void testGroupEditPublicAndPrivateScenario() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentA.getUser())
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.selectFilter("Private")
            .multiSelectScenarios(componentB.getComponentName() + ", " + componentB.getScenarioName())
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14725})
    @Description("Attempt to edit multiple scenarios, including one which is processing")
    public void testGroupEditScenarioInProcessingState() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentA.getUser())
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14726, 15015})
    @Description("Attempt to edit more than 10 scenarios")
    public void testEditMoreThanTenScenarios() {
        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(11);
        List<ComponentInfoBuilder> ten_components = components.subList(0, 10);
        ComponentInfoBuilder excluded_component = components.stream().filter(element -> !ten_components.contains(element)).collect(Collectors.toList()).get(0);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(components.get(0).getUser());
        explorePage.uploadMultiComponentsCID(components);

        components.forEach(component -> softAssertions.assertThat(cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination(50);
        ten_components.forEach(component -> explorePage.multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName()));
        explorePage.publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class);

        explorePage.refresh()
            .setPagination(50)
            .selectFilter("Public");
        ten_components.forEach(component -> explorePage.multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName()));

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.selectFilter("Private")
            .openScenario(excluded_component.getComponentName(), excluded_component.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public");

        ten_components.forEach(component -> explorePage.multiSelectScenarios(component.getComponentName() + "," + component.getScenarioName()));
        explorePage.multiSelectScenarios(excluded_component.getComponentName() + "," + excluded_component.getScenarioName());
        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
