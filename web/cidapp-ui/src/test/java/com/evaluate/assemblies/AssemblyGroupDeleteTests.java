package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AssemblyGroupDeleteTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentInfoBuilder cidComponentItem;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {})
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

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(arc)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(cube50)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(ellipse)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(octagon)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(cube75)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(hexagon)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(cube100)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(slot)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(cuboid)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(cylinder)).isEqualTo(true);

        softAssertions.assertAll();
    }
}
