package com.evaluate.assemblies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UpdateCADFileTests extends TestBase {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String filterName2 = generateStringUtil.generateFilterName();
    private FilterPage filterPage;
    private UserCredentials currentUser;

    private final String componentExtension = ".prt.1";
    private final String modifiedComponentExtension = ".prt.2";
    private final String originalAsmExtension = ".asm.1";
    private final String modifiedAsmExtension = ".asm.2";

    private String autoBotAsm = "autobotasm";
    private String autoHelm = "autoparthelm";
    private String autoHead = "autoparthead";
    private String autoTorso = "autoparttorso";
    private String autoArm = "autopartarm";
    private String autoHand = "autoparthand";
    private String autoLeg = "autopartleg";
    private String autoFoot = "autopartfoot";
    private ComponentInfoBuilder assemblyInfo;

    @After
    public void deleteScenarios() {
        if (currentUser != null) {
            assemblyUtils.deleteAssembly(assemblyInfo, currentUser);
        }
    }
    private File modifiedAutoAsm = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoBotAsm + modifiedAsmExtension);
    private File autoHelmFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHelm + componentExtension);
    private File modifiedAutoHeadFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHead + modifiedComponentExtension);

    @Test
    @TestRail(testCaseId = {"10903", "10908", "10909"})
    @Description("Validate Update CAD file for an assembly scenario that is CAD connected and uncosted")
    public void updateAssemblyCADFileTest() {
        SoftAssertions soft = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<String> components = Arrays.asList(autoHead, autoTorso, autoArm, autoHand, autoLeg, autoFoot);

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoBotAsm, originalAsmExtension, ProcessGroupEnum.ASSEMBLY,
            components, componentExtension, ProcessGroupEnum.ASSEMBLY, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        evaluatePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName);
        componentsListPage = evaluatePage.clickActions()
            .updateCadFile(modifiedAutoAsm)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .openComponents();

        soft.assertThat(componentsListPage.getListOfSubcomponents().contains(autoHelm.toUpperCase())).as("Verify new Sub-Component after CAD update")
            .isTrue();
        soft.assertThat(componentsListPage.isTextDecorationStruckOut(autoHelm)).as("Verify new Sub-Component is struck out").isTrue();

        componentsListPage.multiSelectSubcomponents(autoHelm + "," + scenarioName)
                .updateCadFile(autoHelmFile);

        List<ScenarioItem> autoHelmDetails = componentsUtil.getUnCostedComponent(autoHelm, scenarioName, currentUser);

        ComponentInfoBuilder helmInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoHelmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHelmDetails.get(0).getComponentIdentity())
            .componentName(autoHelm)
            .user(currentUser)
            .build();

        assemblyInfo.addSubComponent(helmInfo);

        componentsListPage.multiSelectSubcomponents(autoHead + "," + scenarioName)
                .updateCadFile(modifiedAutoHeadFile);

        soft.assertThat(componentsListPage.getScenarioState(autoHead, scenarioName))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsListPage.checkSubcomponentState(assemblyInfo, autoHead);
        evaluatePage.clickRefresh(EvaluatePage.class);
        soft.assertThat(componentsListPage.getScenarioState(autoHead, scenarioName))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10928", "10929", "10933", "11967"})
    @Description("Verify enabled/disabled behaviour of Update CAD button in Assembly Explorer Table")
    public void verifyUpdateCADButtonAsmExplorerTableTest() {
        SoftAssertions soft = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<String> components = Arrays.asList(autoHead, autoTorso, autoArm, autoHand, autoLeg, autoFoot);

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoBotAsm, originalAsmExtension, ProcessGroupEnum.ASSEMBLY,
            components, componentExtension, ProcessGroupEnum.ASSEMBLY, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        componentsListPage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName)
            .openComponents();

        soft.assertThat(componentsListPage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled when nothing selected").isFalse();
        componentsListPage.multiSelectSubcomponents(autoArm + "," + scenarioName);
        soft.assertThat(componentsListPage.isCadButtonEnabled()).as("Verify Update CAD file button is enabled").isTrue();
        componentsListPage.multiSelectSubcomponents(autoTorso + "," + scenarioName);
        soft.assertThat(componentsListPage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled after 2 selected").isFalse();

        soft.assertAll();
    }
}
