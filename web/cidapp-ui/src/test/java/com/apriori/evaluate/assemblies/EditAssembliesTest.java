package com.apriori.evaluate.assemblies;

import static com.apriori.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.dto.AssemblyDTORequest;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.response.component.CostingTemplate;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.SetInputStatusPage;
import com.apriori.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.explore.PreviewPage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.ButtonTypeEnum;
import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import com.utils.StatusIconEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBaseUI {

    private static ComponentInfoBuilder componentAssembly;
    private static ComponentInfoBuilder componentAssembly2;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyDTORequest assemblyDTORequest = new AssemblyDTORequest();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private EditScenarioStatusPage editScenarioStatusPage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private InfoPage infoPage;
    private ExplorePage explorePage;
    private EditComponentsPage editComponentsPage;
    private EditScenarioStatusPage editStatusPage;
    private PreviewPage previewPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder cidComponentItem;

    @Test
    @Tag(SMOKE)
    @TestRail(id = 10768)
    @Description("Shallow Publish assembly and scenarios costed in CI Design")
    public void testShallowPublishCostedCID() {
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(big_ring + "," + componentAssembly.getScenarioName(), pin + "," + componentAssembly.getScenarioName(), small_ring + "," + componentAssembly.getScenarioName())
            .publishSubcomponent()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .refresh()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {10799, 6076, 6515})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testShallowEditCostedCID() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName());

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();
        softAssertions.assertThat(evaluatePage.isAnnualVolumeInputEnabled()).isEqualTo(false);
        softAssertions.assertThat(evaluatePage.isAnnualYearsInputEnabled()).isEqualTo(false);

        evaluatePage.editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isTrue();
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10801})
    @Description("Retain the Status/Cost Maturity/Assignee/Lock during a Shallow Edit")
    public void testShallowEditRetainStatus() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10802, 10803, 10835, 6613})
    @Description("Modify the Status/Cost Maturity/Lock after a Shallow Edit and ensure subcomponents are associated")
    public void testShallowEditModifyStatusCheckAssociationSmallSetSubcomponents() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isTrue();

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");

        componentsTablePage = infoPage.cancel(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        componentAssembly.getSubComponents().forEach(subcomponent -> assertThat(componentsTablePage.getListOfSubcomponents(), hasItem(subcomponent.getComponentName().toUpperCase())));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {10804, 6733, 6594})
    @Description("Shallow Edit keeps original assembly intact on Public Workspace")
    public void testShallowEditCheckDuplicate() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(2);

        explorePage.selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {10806, 10807, 10809, 6614})
    @Description("Shallow Edited assemblies and scenarios can be published into Public Workspace and can also add notes and lock/unlock scenario")
    public void testShallowEditPublishPublicWorkspaceLockNotes() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .publishScenario(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();

        evaluatePage.clickActions()
            .lock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isTrue();

        evaluatePage.clickActions()
            .unlock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK)).isTrue();

        evaluatePage.clickActions()
            .info()
            .selectStatus("Analysis")
            .inputCostMaturity("Medium")
            .inputDescription("QA Modified Test Description")
            .inputNotes("Testing Modified QA notes")
            .submit(EvaluatePage.class);

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Modified Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing Modified QA notes");

        softAssertions.assertAll();
    }

    @Disabled("A unique assembly is needed to do this and then some post steps to delete this unique assembly and subcomponents")
    @Test
    @TestRail(id = {10836, 10811})
    @Description("Shallow Edit an assembly with larger set of sub-components ")
    public void testUploadCostPublishAssemblyLargeSetSubcomponents() {

        componentAssembly = assemblyDTORequest.getAssembly("FUSELAGE_SUBASSEMBLY");

        assemblyUtils.uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        componentAssembly.getSubComponents().forEach(subcomponent -> assertThat(componentsTablePage.getListOfSubcomponents(), hasItem(subcomponent.getComponentName().toUpperCase())));
    }

    @Test
    @TestRail(id = {10810})
    @Description("Shallow Edit assembly and scenarios that was uncosted in CI Design")
    public void testUploadUncostedAssemblySubcomponentOverride() {

        final String bigRing = "big ring";
        final String pin = "Pin";
        final String smallRing = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED), is(true));

        componentsTablePage = evaluatePage.openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(pin, componentAssembly.getScenarioName())).contains("circle-minus");
        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, componentAssembly.getScenarioName())).contains("circle-minus");
        softAssertions.assertThat(componentsTablePage.getRowDetails(smallRing, componentAssembly.getScenarioName())).contains("circle-minus");

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {10813, 10815, 11032})
    @Description("Attempt to Shallow Edit over existing Private locked scenarios and renaming")
    public void testShallowEditPrivateLockedRename() {

        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        String refreshMessage = "This assembly has uncosted changes. If you continue, these changes will be lost.";

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editComponentsPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .editScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("A private scenario with this name already exists. The private scenario is locked and cannot be overridden, " +
            "please supply a different scenario name or cancel the operation.");

        evaluatePage = editComponentsPage.enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(newScenarioName)).isTrue();

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_GERMANY)
            .clickRefresh(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getWarningMessageText()).contains(refreshMessage);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10814, 6596, 6046})
    @Description("Shallow Edit over existing Private scenarios with override")
    public void testShallowEditPrivateOverride() {

        componentAssembly = assemblyDTORequest.getAssembly();

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editComponentsPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .editScenario(EditComponentsPage.class)
            .overrideScenarios();

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("A private scenario with this name already exists. Cancel this operation, or select an option below and continue.");

        editStatusPage = editComponentsPage.clickContinue(EditScenarioStatusPage.class);
        softAssertions.assertThat(editStatusPage.getEditScenarioMessage()).contains("Scenario was successfully edited, click here to open in the evaluate view.");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2965")
    @TestRail(id = {10895, 10897})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndOverridePrivateSubcomponent() {

        final String BIG_RING = "big ring";
        final String SMALL_RING = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponent.getProcessGroup().getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + componentAssembly.getScenarioName(), SMALL_RING + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        componentAssembly.getSubComponents().forEach(subcomponentName ->
            assertThat(componentsTreePage.getScenarioState(subcomponentName.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getUser(), ScenarioStateEnum.COST_COMPLETE),
                is(ScenarioStateEnum.COST_COMPLETE.getState())));

        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10896, 10898, 5619, 5428})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndRenamePrivateSubcomponent() {
        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String BIG_RING = "big ring";
        final String SMALL_RING = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + componentAssembly.getScenarioName(), SMALL_RING + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios(BIG_RING, newScenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsTreePage.getListOfScenarios(SMALL_RING, newScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10899)
    @Description("Edit multiple public sub-components with mixture of Public & Private counterparts (Override)")
    public void testEditPublicSubcomponentsMixedWithPrivateThenOverride() {

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        componentAssembly = assemblyDTORequest.getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName(), NUT + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTablePage = editScenarioStatusPage.close(ComponentsTablePage.class);

        componentAssembly.getSubComponents().forEach(componentName ->
            assertThat(componentsTablePage.getScenarioState(componentName.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getUser(), ScenarioStateEnum.COST_COMPLETE),
                is(ScenarioStateEnum.COST_COMPLETE.getState())));

        softAssertions.assertThat(componentsTablePage.getListOfScenarios(BOLT, componentAssembly.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(componentsTablePage.getListOfScenarios(NUT, componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10900)
    @Description("Edit multiple public sub-components with mixture of Public & Private counterparts (Rename)")
    public void testEditPublicSubcomponentsMixedWithPrivateThenRename() {

        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        componentAssembly = assemblyDTORequest.getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(FLANGE + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class)
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName(), NUT + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios(BOLT, newScenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsTreePage.getListOfScenarios(NUT, newScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11142)
    @Description("Validate an error message appears if any issues occur")
    public void testEditWithExistingPrivateScenarioName() {
        String preExistingScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        ComponentInfoBuilder preExistingComponentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            preExistingScenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(preExistingScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioErrorMessage(), containsString("failed while attempting to create private scenario(s)."));
    }

    @Test
    @TestRail(id = 10810)
    @Description("Shallow Edit an assembly with uncosted scenarios")
    public void testShallowEditCostedAssemblyWithUncostedSubComponents() {

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);

        componentsTreePage = evaluatePage.openComponents()
            .checkSubcomponentState(componentAssembly, BIG_RING + "," + SMALL_RING + "," + PIN);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            assertThat(componentsTreePage.getRowDetails(subcomponent.getComponentName(), componentAssembly.getScenarioName()), hasItem(StatusIconEnum.PUBLIC.getStatusIcon())));

        componentAssembly.getSubComponents().forEach(subcomponent ->
            softAssertions.assertThat(componentsTreePage.getListOfScenariosWithStatus(subcomponent.getComponentName(), componentAssembly.getScenarioName(), ScenarioStateEnum.NOT_COSTED)).isEqualTo(true));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12040, 11954, 6521, 10874, 11027})
    @Description("Validate I can switch between public sub components when private iteration is deleted")
    public void testSwitchingPublicSubcomponentsWithDeletedPrivateIteration() {

        final String BOLT = "bolt";

        componentAssembly = assemblyDTORequest.getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(componentAssembly.getSubComponents().stream().findFirst().get().getProcessGroup().getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, BOLT)
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName())
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsTablePage.class)
            .closePanel()
            .clickExplore()
            .getCssComponents(componentAssembly.getUser(), "componentName[EQ], " + BOLT, "scenarioName[EQ], " + componentAssembly.getScenarioName(),
                "scenarioState[EQ], " + ScenarioStateEnum.COST_COMPLETE, "scenarioPublished[EQ], false", "iteration[EQ], 2")
            .refresh()
            .selectFilter("Private")
            .clickSearch(BOLT)
            .multiSelectScenarios("" + BOLT + ", " + componentAssembly.getScenarioName() + "")
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .navigateToScenario(componentAssembly)
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(BOLT, componentAssembly.getScenarioName()))
            .as("Verify that deleted sub-component replaced with Public 'missing' scenario").contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(BOLT))
            .as("Verify that deleted sub-component replaced with 'missing' scenario").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2965")
    @TestRail(id = {12037})
    @Description("Validate I can switch between public sub components")
    public void testSwitchBetweenPublicSubcomponents() {
        String editedComponentScenarioName = new GenerateStringUtil().generateScenarioName();

        final String PIN = "Pin";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponent.getProcessGroup().getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly);

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        componentsTablePage = evaluatePage.openComponents()
            .selectTableView()
            .multiSelectSubcomponents(PIN + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .multiSelectSubcomponents(PIN + "," + componentAssembly.getScenarioName())
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .multiSelectSubcomponents(PIN + "," + componentAssembly.getScenarioName())
            .publishSubcomponent()
            .changeName(editedComponentScenarioName)
            .clickContinue(PublishPage.class)
            .publish(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(PIN, editedComponentScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        evaluatePage = componentsTablePage.closePanel()
            .clickCostButton()
            .confirmCost("Yes")
            .waitForCostLabelNotContain(NewCostingLabelEnum.COSTING_IN_PROGRESS, 2);

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11133, 11141})
    @Description("Validate the edit button will only be enabled when top level sub components are selected")
    public void testEditButtonSubAssembly() {

        componentAssembly = assemblyDTORequest.getAssembly("assy03A");
        componentAssembly2 = assemblyDTORequest.getAssembly("assy03");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);
        assemblyUtils.costSubComponents(componentAssembly2).costAssembly(componentAssembly2);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly2).publishAssembly(componentAssembly2);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly2)
            .openComponents()
            .expandSubAssembly(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .multiSelectSubcomponents(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName() + "", "Part0005a" + "," + componentAssembly.getScenarioName() + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents("Part0005a" + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 6595)
    @Description("Validate that ONLY the selected assembly scenario is copied to the private workspace and not the components within it")
    public void testEditPublicAssembly() {

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        componentAssembly = assemblyDTORequest.getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .setPagination();

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(2);
        softAssertions.assertThat(explorePage.getListOfScenarios(FLANGE, componentAssembly.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(NUT, componentAssembly.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(BOLT, componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6601, 6602, 11869, 12022, 12023, 6522})
    @Description("Validate user can open a public component from a private workspace")
    public void testOpeningPublicComponentFromPrivateWorkspace() {

        final String PIN = "Pin";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(PIN, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        previewPage = componentsTreePage.clickScenarioCheckbox(PIN)
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.isPreviewPanelDisplayed()).isTrue();

        componentsTreePage = previewPage.closePreviewPanelOnComponentsPage();

        softAssertions.assertThat(componentsTreePage.isPreviewPanelDisplayed()).isFalse();

        evaluatePage = componentsTreePage.openAssembly(PIN, componentAssembly.getScenarioName());

        softAssertions.assertThat(evaluatePage.getTabTitle()).contains("PIN");
        softAssertions.assertThat(evaluatePage.getTabCount()).isEqualTo(2);

        evaluatePage.editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11960})
    @Description("Validate a private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditPublicAssemblyAssociationsPrivatePreference() {

        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(componentAssembly.getScenarioName());

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(componentAssembly.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11961, 11956})
    @Description("Validate a new private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditPublicAssemblyAssociationsPrivateNewScenarioPreferenceAndDelete() {

        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";

        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(componentAssembly.getUser())
            .uploadComponent(big_ring, newScenarioName, componentAssembly.getSubComponents().stream().findAny()
                .filter(x -> x.getComponentName().equalsIgnoreCase(big_ring))
                .get()
                .getResourceFile(), componentAssembly.getUser());

        evaluatePage = new EvaluatePage(driver).refresh()
            .navigateToScenario(componentAssembly);

        componentsTreePage = evaluatePage.openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(componentAssembly.getScenarioName());

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(componentAssembly.getScenarioName());

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + componentAssembly.getScenarioName())
            .deleteSubcomponent()
            .clickDelete(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .clickCostButton()
            .confirmCost("Yes")
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.SCENARIO_TYPE);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, componentAssembly.getScenarioName())).contains(StatusIconEnum.MISSING.getStatusIcon(),
            StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10843})
    @Description("Validate assembly explorer table updates when sub-components changed")
    public void testAssemblyExplorerTableUpdates() {

        final String BIG_RING = "big ring";
        final String PIN = "Pin";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTreePage.multiSelectSubcomponents(PIN + "," + componentAssembly.getScenarioName())
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(componentAssembly.getSubComponents().get(0).getProcessGroup())
            .enterAnnualVolume("1234")
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class);

        scenariosUtil.getScenarioCompleted(componentAssembly.getSubComponents().get(1));
        componentsTreePage = componentsTreePage.closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(PIN, componentAssembly.getScenarioName())).as("Verify details updated")
            .contains(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory(), "1,234");

        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        evaluatePage = componentsTreePage.closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + componentAssembly.getScenarioName())
            .editSubcomponent(EditScenarioStatusPage.class)
            .clickHere()
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_EDIT_ACTION, 2);

        evaluatePage.enterAnnualVolume("7777")
            .goToAdvancedTab()
            .enterBatchSize("612")
            .goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2);

        componentsTreePage = evaluatePage.clickExplore()
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, componentAssembly.getScenarioName())).as("Verify details updated")
            .contains("7,777", "612");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10860, 21552})
    @Description("Validate 'missing' scenario created if sub-component deleted'")
    public void testMissingSubComponentOnDeletion() {

        final String SMALL_RING = "small ring";

        componentAssembly = assemblyDTORequest.getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        ComponentInfoBuilder smallRing = assemblyUtils.getSubComponent(componentAssembly, SMALL_RING);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(SMALL_RING + "," + componentAssembly.getScenarioName())
            .deleteSubcomponent()
            .clickDelete(ComponentsTreePage.class);

        scenariosUtil.checkComponentDeleted(smallRing.getComponentIdentity(), smallRing.getScenarioIdentity(), componentAssembly.getUser());

        componentsTreePage = componentsTreePage.closePanel().clickRefresh(EvaluatePage.class).openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(SMALL_RING)).as("Verify 'missing' Small Ring Struck Out").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, componentAssembly.getScenarioName()))
            .as("Verify that 'missing' Small Ring is CAD Disconnected").contains(StatusIconEnum.DISCONNECTED.getStatusIcon());

        softAssertions.assertAll();
    }
}
