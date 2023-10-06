package com.apriori.evaluate.assemblies;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AssemblyGroupDeleteTests extends TestBaseUI {

    private UserCredentials currentUser;
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {15023, 15024, 15025, 15026})
    @Description("Delete 10 subcomponents of an assembly")
    public void assemblyMenuGroupDelete() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String assemblyName = "RandomShapeAsm";
        String assemblyExtension = ".SLDASM";
        String subComponentExtension = ".SLDPRT";
        String arc = "50mmArc";
        String cube50 = "50mmCube";
        String ellipse = "50mmEllipse";
        String octagon = "50mmOctagon";
        String cube75 = "75mmCube";
        String hexagon = "75mmHexagon";
        String cube100 = "100mmCube";
        String slot = "100mmSlot";
        String cuboid = "150mmCuboid";
        String cylinder = "200mmCylinder";
        String blob = "500mmBlob";

        final List<String> subComponentNames = Arrays.asList(
            arc, cube50, ellipse, octagon, cube75, hexagon, cube100, slot, cuboid, cylinder, blob);

        final List<String> subComponentNamesToDelete = Arrays.asList(
            arc, cube50, ellipse, octagon, cube75, hexagon, cube100, slot, cuboid, cylinder);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            processGroupEnum,
            subComponentNames,
            subComponentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .openScenario(assemblyName, scenarioName);
        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isFalse();

        componentsTreePage.multiSelectSubcomponents(arc + "," + scenarioName, cube50 + "," + scenarioName, ellipse + "," + scenarioName, octagon + "," + scenarioName,
            cube75 + "," + scenarioName, hexagon + "," + scenarioName, cube100 + "," + scenarioName, slot + "," + scenarioName, cuboid + "," + scenarioName,
            cylinder + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isTrue();

        componentsTreePage.multiSelectSubcomponents(blob + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isFalse();

        componentsTreePage.multiSelectSubcomponents(blob + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isTrue();

        componentsTreePage.deleteSubcomponent()
            .clickDelete(DeletePage.class)
            .clickClose(ComponentsTreePage.class)
            .checkComponentDeleted(componentAssembly, arc, cube50, ellipse, octagon, cube75, hexagon, cube100, slot, cuboid, cylinder)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        subComponentNamesToDelete.forEach(comp ->
            softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(comp)).isEqualTo(true));

        softAssertions.assertAll();
    }
}
