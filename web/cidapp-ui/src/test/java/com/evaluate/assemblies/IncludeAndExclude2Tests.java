package com.evaluate.assemblies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import com.utils.ColourEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IncludeAndExclude2Tests extends TestBase {

    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private EvaluatePage evaluatePage;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();

    public IncludeAndExclude2Tests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "12089")
    @Description("Verify Excluded scenarios are not highlighted in flattened view")
    public void testExcludedScenarioInFlattenedView() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .multiSelectSubcomponents("PIN, " + scenarioName + "", "SMALL RING, " + scenarioName + "")
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .tableView();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(componentsListPage.getCellColour("pin", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());
        softAssertions.assertThat(componentsListPage.getCellColour("small ring", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11921", "11920", "11919"})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeSubcomponentsAndCost() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

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

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .closePanel()
            .costScenario()
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.INCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(false)));

        evaluatePage = componentsListPage.closePanel()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = {"11918", "11917", "11916"})
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeSubcomponentsAndCost() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList("flange", "nut", "bolt");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

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

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(true)));

        evaluatePage = componentsListPage.closePanel()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }
}
