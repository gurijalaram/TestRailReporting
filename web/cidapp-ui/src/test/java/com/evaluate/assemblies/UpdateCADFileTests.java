package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UpdateCADFileTests extends TestBase {
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;

    private final String componentExtension = ".prt.1";
    private final String modifiedComponentExtension = ".prt.2";
    private final String originalAsmExtension = ".asm.1";
    private final String modifiedAsmExtension = ".asm.2";
    private final String topLevelAsmExtension = ".asm.3";

    private String autoBotAsm = "autobotasm";
    private String autoHelm = "autoparthelm";
    private String autoHead = "autoparthead";
    private String autoTorso = "autoparttorso";
    private String autoArm = "autopartarm";
    private String autoHand = "autoparthand";
    private String autoLeg = "autopartleg";
    private String autoFoot = "autopartfoot";
    private String autoSword = "autosword";
    private String autoPommel = "autopommel";
    private String autoHandle = "autohandle";
    private String autoGuard = "autoguard";
    private String autoBlade = "autoblade";
    private ComponentInfoBuilder assemblyInfo;
    private ComponentInfoBuilder subAssemblyInfo;

    @After
    public void deleteScenarios() {
        if (currentUser != null) {
            assemblyUtils.deleteAssemblyAndComponents(assemblyInfo, currentUser);
            assemblyInfo = null;
        }
        if (subAssemblyInfo != null) {
            assemblyUtils.deleteAssemblyAndComponents(subAssemblyInfo, currentUser);
            subAssemblyInfo = null;
        }
    }

    private File modifiedAutoAsm = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoBotAsm + modifiedAsmExtension);
    private File autoHelmFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHelm + componentExtension);
    private File modifiedAutoHeadFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHead + modifiedComponentExtension);
    private File autoArmFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoArm + componentExtension);
    private File autoSwordFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoSword + originalAsmExtension);
    private File autoHandleFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHandle + componentExtension);

    @Test
    @TestRail(testCaseId = {"10903", "10961"})
    @Description("Validate Update CAD file for an assembly scenario then update CAD file via Components Table for missing sub-component")
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
        componentsTreePage = evaluatePage.clickActions()
            .updateCadFile(modifiedAutoAsm)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .openComponents();

        soft.assertThat(componentsTreePage.getListOfSubcomponents().contains(autoHelm.toUpperCase())).as("Verify new Sub-Component after CAD update")
            .isTrue();
        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHelm)).as("Verify new Sub-Component is struck out").isTrue();

        componentsTreePage.multiSelectSubcomponents(autoHelm + "," + scenarioName)
            .updateCadFile(autoHelmFile);

        List<ScenarioItem> autoHelmDetails = componentsUtil.getUnCostedComponent(autoHelm, scenarioName, currentUser);

        ComponentInfoBuilder helmInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoHelmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHelmDetails.get(0).getComponentIdentity())
            .componentName(autoHelm)
            .user(currentUser)
            .build();

        assemblyInfo.getSubComponents().add(helmInfo);
        soft.assertThat(componentsTreePage.getScenarioState(autoHelm, scenarioName))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(assemblyInfo, autoHelm);
        evaluatePage.clickRefresh(EvaluatePage.class);
        soft.assertThat(componentsTreePage.getScenarioState(autoHelm, scenarioName))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");

        componentsTreePage.multiSelectSubcomponents(autoHead + "," + scenarioName)
                .updateCadFile(modifiedAutoHeadFile);

        soft.assertThat(componentsTreePage.getScenarioState(autoHead, scenarioName))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(assemblyInfo, autoHead);
        evaluatePage.clickRefresh(EvaluatePage.class);
        soft.assertThat(componentsTreePage.getScenarioState(autoHead, scenarioName))
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

        scenarioUtil.publishScenario(assemblyInfo.getSubComponents().get(5), null, HttpStatus.SC_CREATED);

        componentsTreePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName)
            .openComponents();

        soft.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled when nothing selected").isFalse();
        componentsTreePage.multiSelectSubcomponents(autoArm + "," + scenarioName);
        soft.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is enabled").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoTorso + "," + scenarioName);
        soft.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled after 2 selected").isFalse();

        componentsTreePage.multiSelectSubcomponents(autoArm + "," + scenarioName, autoTorso + "," + scenarioName);

        soft.assertThat(componentsTreePage.isIconDisplayed(StatusIconEnum.PUBLIC, autoFoot.toUpperCase())).as("Verify sub-component is Public").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoFoot + "," + scenarioName);
        soft.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled after Public sub-component selected").isFalse();

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11965")
    @Description("Validate updating of CAD file for the sub-component of a sub-assembly via components table.")
    public void updateSubAssemblyCADFilesFromComponentTableTest() {
        SoftAssertions soft = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<String> components = Arrays.asList(autoHelm, autoHead, autoTorso, autoArm, autoHand, autoLeg, autoFoot);
        List<String> subAsmComponents = Arrays.asList(autoPommel, autoGuard, autoBlade);

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoBotAsm, topLevelAsmExtension, ProcessGroupEnum.ASSEMBLY,
            components, componentExtension, ProcessGroupEnum.ASSEMBLY, scenarioName, currentUser);

        subAssemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoSword, originalAsmExtension, ProcessGroupEnum.ASSEMBLY,
            subAsmComponents, componentExtension, ProcessGroupEnum.ASSEMBLY, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(subAssemblyInfo);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        evaluatePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName);
        componentsTreePage = evaluatePage.openComponents();

        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword)).as("Verify Missing Sub-Assembly is struck out").isTrue();
        soft.assertThat(componentsTreePage.getRowDetails(autoSword, scenarioName).contains(StatusIconEnum.DISCONNECTED.getStatusIcon()))
                .as("Verify sub-assembly is shown as CAD disconnected").isTrue();

        componentsTreePage.multiSelectSubcomponents(autoSword + "," + scenarioName)
            .updateCadFile(autoSwordFile);

        List<ScenarioItem> autoSwordDetails = componentsUtil.getUnCostedComponent(autoSword, scenarioName, currentUser);
        subAssemblyInfo.setComponentIdentity(autoSwordDetails.get(0).getComponentIdentity());
        subAssemblyInfo.setScenarioIdentity(autoSwordDetails.get(0).getScenarioIdentity());

        soft.assertThat(componentsTreePage.getScenarioState(autoSword, scenarioName))
            .as("Verify that sub-assembly CAD file update is being processed").isEqualTo("gear");
        scenarioUtil.getScenarioCompleted(subAssemblyInfo);
        evaluatePage.clickRefresh(EvaluatePage.class);
        soft.assertThat(componentsTreePage.getScenarioState(autoSword, scenarioName))
            .as("Verify that sub-assembly CAD file update completed successfully").isEqualTo("circle-minus");

        componentsTreePage.expandSubAssembly(autoSword, scenarioName);
        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHandle)).as("Verify Missing Sub-Assembly sub-component is struck out").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoHandle + "," + scenarioName)
            .updateCadFile(autoHandleFile);

        List<ScenarioItem> autoHandleDetails = componentsUtil.getUnCostedComponent(autoHandle, scenarioName, currentUser);

        ComponentInfoBuilder handleInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoHandleDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHandleDetails.get(0).getComponentIdentity())
            .componentName(autoHandle)
            .user(currentUser)
            .build();

        subAssemblyInfo.getSubComponents().add(handleInfo);

        componentsTreePage.expandSubAssembly(autoSword, scenarioName);
        soft.assertThat(componentsTreePage.getScenarioState(autoHandle, scenarioName))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(subAssemblyInfo, autoHandle);
        evaluatePage.clickRefresh(EvaluatePage.class);
        componentsTreePage.expandSubAssembly(autoSword, scenarioName);
        soft.assertThat(componentsTreePage.getScenarioState(autoHandle, scenarioName))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");
        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHandle)).as("Verify Missing Sub-Assembly sub-component is not struck out").isFalse();
        soft.assertThat(componentsTreePage.getRowDetails(autoHandle, scenarioName).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Missing part now CAD Connected").isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10908", "10909", "12131"})
    @Description("Validate updating sub-assembly and sub-component CAD files by opening new tab from Components Table.")
    public void updateSubComponentCADFileTest() {
        SoftAssertions soft = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<String> components = Arrays.asList(autoHead, autoTorso, autoHand, autoLeg, autoFoot);

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoBotAsm, originalAsmExtension, ProcessGroupEnum.ASSEMBLY,
            components, componentExtension, ProcessGroupEnum.ASSEMBLY, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        componentsTreePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName)
            .openComponents();

        soft.assertThat(componentsTreePage.getListOfSubcomponents().contains(autoArm.toUpperCase())).as("Verify missing Arm component added as virtual part")
            .isTrue();
        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoArm)).as("Verify Arm sub-component is struck out").isTrue();

        componentsTreePage.openAssembly(autoArm, scenarioName)
            .clickActions()
            .updateCadFile(autoArmFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .closeNewlyOpenedTab()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoArm))
            .as("Verify Arm sub-component is no longer struck out").isFalse();
        soft.assertThat(componentsTreePage.getRowDetails(autoArm, scenarioName).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify CAD connected icon is present");

        List<ScenarioItem> autoArmDetails = componentsUtil.getUnCostedComponent(autoArm, scenarioName, currentUser);
        ComponentInfoBuilder armInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoArmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoArmDetails.get(0).getComponentIdentity())
            .componentName(autoArm)
            .user(currentUser)
            .build();

        assemblyInfo.getSubComponents().add(armInfo);

        componentsTreePage.openAssembly(autoHead, scenarioName)
            .clickActions()
            .updateCadFile(modifiedAutoHeadFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .closeNewlyOpenedTab()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        soft.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHead))
            .as("Verify Head sub-component is no longer struck out").isFalse();
        soft.assertThat(componentsTreePage.getRowDetails(autoHead, scenarioName).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify CAD connected icon is present");

        soft.assertAll();
    }
}
