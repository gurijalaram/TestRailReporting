package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;
    private ComponentsListPage componentsListPage;
    private ExplorePage explorePage;

    final AssemblyUtils assemblyUtils = new AssemblyUtils();

    public EditAssembliesTest() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"10799", "10768", "10801", "10802", "10803", "10804", "10810"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyAndEditAddNotes() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));

        evaluatePage.editScenario()
            .close(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION), is(true));
        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE), is(true));

        evaluatePage.lock(EvaluatePage.class)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class);

        evaluatePage.publishScenario()
            .publish(componentAssembly, currentUser, EvaluatePage.class);

        infoPage = evaluatePage.info();
        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Low")));
        assertThat(infoPage.getDescription(), is(equalTo("QA Test Description")));
        assertThat(infoPage.getNotes(), is(equalTo("Testing QA notes")));

        componentsListPage = infoPage.cancel(EvaluatePage.class)
            .openComponents();

        subComponentNames.forEach(subcomponent -> assertThat(componentsListPage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));

        explorePage = componentsListPage.closePanel()
            .clickExplore()
            .selectFilter("Recent");

        assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName), is(equalTo(2)));
    }

    @Test
    @TestRail(testCaseId = {"10806", "10807", "10809", "10835"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyAndOverrideLockNotes() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .publishScenario()
            .override()
            .clickContinue(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));

        explorePage = evaluatePage.clickExplore()
            .selectFilter("Public");

        assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName), is(greaterThanOrEqualTo(1)));

        evaluatePage = explorePage.navigateToScenario(componentAssembly)
            .lock(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK), is(true));

        evaluatePage.clickExplore()
            .selectFilter("Private")
            .openScenario(assemblyName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class);

        infoPage = evaluatePage.info();
        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Low")));
        assertThat(infoPage.getDescription(), is(equalTo("QA Test Description")));
        assertThat(infoPage.getNotes(), is(equalTo("Testing QA notes")));

        componentsListPage = infoPage.cancel(EvaluatePage.class)
            .openComponents();

        subComponentNames.forEach(subcomponent -> assertThat(componentsListPage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"10836"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyLargeSubcomponentSet() {
        final String assemblyName = "FUSELAGE_SUBASSEMBLY";
        final String assemblyExtension = ".CATPart";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String subcomponent1 = "505-04426-001 1 1 ---";
        final String subcomponent2 = "550-05628-401 PRIMARY 1 ---";
        final String subcomponent3 = "550-05676-001 1 1 ---";
        final String subcomponent4 = "MS14108-3 1 ---";
        final List<String> uploadedSubcomponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3, subcomponent4);
        final String subComponentExtension = ".CATPart";
        final List<String> allSubComponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3, subcomponent4, "505-04596-001 1 1 ---", "550-05526-001 1 1 --A", "550-05629-401 PRIMARY 1 ---", "550-05673-401 PRIMARY 1 ---",
            "550-05676-002 1 1 ---", "550-05682-001 1 1 --A", "550-05683-001 1 1 ---", "550-05683-002 1 1 ---", "550-05689-001 1 1 ---", "550-05690-001 1 1 ---", "CCR244SS-3-2 1 ---",
            "MS14108-15 1 ---", "MS14218AD4-4 1 ---", "MS20392-1C15 1 ---", "MS20470AD4-5 1 ---", "MS20470AD4-6 1 ---", "MS21059L3 1 ---", "MS21059L08 1 ---", "MS24665-132 1 ---", "NAS1789-3 1 ---", "NAS9309M-6-04 1 ---");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            uploadedSubcomponents,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario()
            .close(EvaluatePage.class)
            .openComponents();

        allSubComponents.forEach(subcomponent -> assertThat(componentsListPage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }
}
