package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
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
    private UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private EditScenarioStatusPage editScenarioStatusPage;

    public EditAssembliesTest() {
        super();
    }

    @Test
    @TestRail(testCaseId = "10810")
    @Description("Shallow Edit an assembly with scenarios uncosted")
    public void testUploadPublishAssemblyAndEdit() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadPublishAndOpenAssembly(
                subComponentNames,
                componentExtension,
                processGroupEnum,
                assemblyName,
                assemblyExtension,
                scenarioName,
                currentUser)
            .editScenario()
            .close(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION), is(true));
        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE), is(true));
    }

    @Test
    @TestRail(testCaseId = {"10799", "10768"})
    @Description("Shallow Edit assembly and scenarios that was cost in CI Design")
    public void testUploadCostPublishAssemblyAndEdit() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final String mode = "Manual";
        final String material = "Steel, Cold Worked, AISI 1010";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .uploadCostPublishAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                mode,
                material,
                currentUser)
            .editScenario()
            .close(EvaluatePage.class);
    }

    @Test
    @TestRail(testCaseId = "10895")
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndOverridePrivateSubcomponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
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
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName, SMALL_RING + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));
    }
}
