package com.apriori.cid.ui.tests.evaluate.assemblies;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.UpdateCadFilePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class UpdateCADFileTests extends TestBaseUI {
    private final String componentExtension = ".prt.1";
    private final String modifiedComponentExtension = ".prt.2";
    private final String originalAsmExtension = ".asm.1";
    private final String modifiedAsmExtension = ".asm.2";
    private final String autoBotAsm = "autobotasm";
    private final String autoHelm = "autoparthelm";
    private final String autoHead = "autoparthead";
    private final String autoTorso = "autoparttorso";
    private final String autoArm = "autopartarm";
    private final String autoFoot = "autopartfoot";
    private final String autoSword = "autosword";
    private final String autoHandle = "autohandle";

    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private UpdateCadFilePage updateCadFilePage;
    private ScenariosUtil scenarioUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder assemblyInfo;
    private ComponentInfoBuilder subAssemblyInfo;

    @AfterEach
    public void deleteScenarios() {
        if (assemblyInfo.getUser() != null) {
            assemblyUtils.deleteAssemblyAndComponents(assemblyInfo);
            assemblyInfo = null;
        }
        if (subAssemblyInfo != null) {
            assemblyUtils.deleteAssemblyAndComponents(subAssemblyInfo);
            subAssemblyInfo = null;
        }
    }

    @Test
    @TestRail(id = {10903, 10961, 12032})
    @Description("Validate Update CAD file for an assembly scenario then update CAD file via Components Table for missing sub-component")
    public void updateAssemblyCADFileTest() {
        final File modifiedAutoAsm = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoBotAsm + modifiedAsmExtension);
        final File autoHelmFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHelm + componentExtension);
        final File modifiedAutoHeadFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHead + modifiedComponentExtension);

        assemblyInfo = new AssemblyRequestUtil().getAssembly(autoBotAsm);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        updateCadFilePage = new CidAppLoginPage(driver).login(assemblyInfo.getUser())
            .openScenario(assemblyInfo.getComponentName(), assemblyInfo.getScenarioName())
            .clickActions()
            .updateCadFile(modifiedAutoAsm);

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("No Assembly Association Strategy has been selected. " +
            "The default strategy: Prefer Private Scenarios will be used until updated in User Preferences.");

        componentsTreePage = updateCadFilePage.submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getListOfSubcomponents().contains(autoHelm.toUpperCase())).as("Verify new Sub-Component after CAD update")
            .isTrue();
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHelm)).as("Verify new Sub-Component is struck out").isTrue();

        componentsTreePage.multiSelectSubcomponents(autoHelm + "," + assemblyInfo.getScenarioName())
            .updateCadFile(autoHelmFile);

        List<ScenarioItem> autoHelmDetails = componentsUtil.getUnCostedComponent(autoHelm, assemblyInfo.getScenarioName(), assemblyInfo.getUser());

        ComponentInfoBuilder helmInfo = ComponentInfoBuilder.builder()
            .scenarioName(assemblyInfo.getScenarioName())
            .scenarioIdentity(autoHelmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHelmDetails.get(0).getComponentIdentity())
            .componentName(autoHelm)
            .user(assemblyInfo.getUser())
            .build();

        assemblyInfo.getSubComponents().add(helmInfo);
        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHelm, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(assemblyInfo, autoHelm);

        evaluatePage = new EvaluatePage(driver);

        componentsTreePage = evaluatePage.clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHelm, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");

        componentsTreePage.multiSelectSubcomponents(autoHead + "," + assemblyInfo.getScenarioName())
            .updateCadFile(modifiedAutoHeadFile);

        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHead, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(assemblyInfo, autoHead);
        evaluatePage.clickRefresh(EvaluatePage.class);
        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHead, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10928, 10929, 10933, 11967})
    @Description("Verify enabled/disabled behaviour of Update CAD button in Assembly Explorer Table")
    public void verifyUpdateCADButtonAsmExplorerTableTest() {
        assemblyInfo = new AssemblyRequestUtil().getAssembly("autoBotAsm");

        assemblyUtils.uploadSubComponents(assemblyInfo)
            .uploadAssembly(assemblyInfo);

        scenarioUtil.publishScenario(assemblyInfo.getSubComponents().get(5), null, HttpStatus.SC_CREATED);

        componentsTreePage = new CidAppLoginPage(driver).login(assemblyInfo.getUser())
            .openScenario(assemblyInfo.getComponentName(), assemblyInfo.getScenarioName())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled when nothing selected").isFalse();
        componentsTreePage.multiSelectSubcomponents(autoArm + "," + assemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is enabled").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoTorso + "," + assemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled after 2 selected").isFalse();

        componentsTreePage.multiSelectSubcomponents(autoArm + "," + assemblyInfo.getScenarioName(), autoTorso + "," + assemblyInfo.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isIconDisplayed(StatusIconEnum.PUBLIC, autoFoot.toUpperCase())).as("Verify sub-component is Public").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoFoot + "," + assemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.isCadButtonEnabled()).as("Verify Update CAD file button is disabled after Public sub-component selected").isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11965)
    @Description("Validate updating of CAD file for the sub-component of a sub-assembly via components table.")
    public void updateSubAssemblyCADFilesFromComponentTableTest() {
        final File autoSwordFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoSword + originalAsmExtension);
        final File autoHandleFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHandle + componentExtension);

        assemblyInfo = new AssemblyRequestUtil().getAssembly(autoBotAsm);
        subAssemblyInfo = new AssemblyRequestUtil().getAssembly(autoBotAsm).getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(autoSword)).findFirst().get();

        assemblyUtils.uploadSubComponents(subAssemblyInfo);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        evaluatePage = new CidAppLoginPage(driver).login(assemblyInfo.getUser())
            .openScenario(assemblyInfo.getComponentName(), assemblyInfo.getScenarioName());
        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword)).as("Verify Missing Sub-Assembly is struck out").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword, subAssemblyInfo.getScenarioName()).contains(StatusIconEnum.DISCONNECTED.getStatusIcon()))
            .as("Verify sub-assembly is shown as CAD disconnected").isTrue();

        componentsTreePage.multiSelectSubcomponents(autoSword + "," + subAssemblyInfo.getScenarioName())
            .updateCadFile(autoSwordFile);

        List<ScenarioItem> autoSwordDetails = componentsUtil.getUnCostedComponent(autoSword, subAssemblyInfo.getScenarioName(), subAssemblyInfo.getUser());
        subAssemblyInfo.setComponentIdentity(autoSwordDetails.get(0).getComponentIdentity());
        subAssemblyInfo.setScenarioIdentity(autoSwordDetails.get(0).getScenarioIdentity());

        softAssertions.assertThat(componentsTreePage.getScenarioState(autoSword, subAssemblyInfo.getScenarioName()))
            .as("Verify that sub-assembly CAD file update is being processed").isEqualTo("gear");
        scenarioUtil.getScenarioCompleted(subAssemblyInfo);
        evaluatePage.clickRefresh(EvaluatePage.class);
        softAssertions.assertThat(componentsTreePage.getScenarioState(autoSword, subAssemblyInfo.getScenarioName()))
            .as("Verify that sub-assembly CAD file update completed successfully").isEqualTo("circle-minus");

        componentsTreePage.expandSubAssembly(autoSword, subAssemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHandle)).as("Verify Missing Sub-Assembly sub-component is struck out").isTrue();
        componentsTreePage.multiSelectSubcomponents(autoHandle + "," + assemblyInfo.getScenarioName())
            .updateCadFile(autoHandleFile);

        List<ScenarioItem> autoHandleDetails = componentsUtil.getUnCostedComponent(autoHandle, assemblyInfo.getScenarioName(), assemblyInfo.getUser());

        ComponentInfoBuilder handleInfo = ComponentInfoBuilder.builder()
            .scenarioName(assemblyInfo.getScenarioName())
            .scenarioIdentity(autoHandleDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHandleDetails.get(0).getComponentIdentity())
            .componentName(autoHandle)
            .user(assemblyInfo.getUser())
            .build();

        subAssemblyInfo.getSubComponents().add(handleInfo);

        componentsTreePage.expandSubAssembly(autoSword, subAssemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHandle, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update is being processed").isEqualTo("gear");
        componentsTreePage.checkSubcomponentState(subAssemblyInfo, autoHandle);
        evaluatePage.clickRefresh(EvaluatePage.class);
        componentsTreePage.expandSubAssembly(autoSword, subAssemblyInfo.getScenarioName());
        softAssertions.assertThat(componentsTreePage.getScenarioState(autoHandle, assemblyInfo.getScenarioName()))
            .as("Verify that CAD file update completed successfully").isEqualTo("circle-minus");
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHandle)).as("Verify Missing Sub-Assembly sub-component is not struck out").isFalse();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoHandle, assemblyInfo.getScenarioName()).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Missing part now CAD Connected").isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10908, 10909, 12131})
    @Description("Validate updating sub-assembly and sub-component CAD files by opening new tab from Components Table.")
    public void updateSubComponentCADFileTest() {
        final File modifiedAutoHeadFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoHead + modifiedComponentExtension);
        final File autoArmFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, autoArm + componentExtension);

        assemblyInfo = new AssemblyRequestUtil().getAssembly(autoBotAsm);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        componentsTreePage = new CidAppLoginPage(driver).login(assemblyInfo.getUser())
            .openScenario(autoBotAsm, assemblyInfo.getScenarioName())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getListOfSubcomponents().contains(autoArm.toUpperCase())).as("Verify missing Arm component added as virtual part")
            .isTrue();
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoArm)).as("Verify Arm sub-component is struck out").isTrue();

        componentsTreePage.openAssembly(autoArm, assemblyInfo.getScenarioName())
            .clickActions()
            .updateCadFile(autoArmFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .closeNewlyOpenedTab()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoArm))
            .as("Verify Arm sub-component is no longer struck out").isFalse();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoArm, assemblyInfo.getScenarioName()).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify CAD connected icon is present");

        List<ScenarioItem> autoArmDetails = componentsUtil.getUnCostedComponent(autoArm, assemblyInfo.getScenarioName(), assemblyInfo.getUser());
        ComponentInfoBuilder armInfo = ComponentInfoBuilder.builder()
            .scenarioName(assemblyInfo.getScenarioName())
            .scenarioIdentity(autoArmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoArmDetails.get(0).getComponentIdentity())
            .componentName(autoArm)
            .user(assemblyInfo.getUser())
            .build();

        assemblyInfo.getSubComponents().add(armInfo);

        componentsTreePage.openAssembly(autoHead, assemblyInfo.getScenarioName())
            .clickActions()
            .updateCadFile(modifiedAutoHeadFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .closeNewlyOpenedTab()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoHead))
            .as("Verify Head sub-component is no longer struck out").isFalse();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoHead, assemblyInfo.getScenarioName()).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify CAD connected icon is present");

        softAssertions.assertAll();
    }
}
