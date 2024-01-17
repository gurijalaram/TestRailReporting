package com.apriori.cid.ui.tests.evaluate.assemblies;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.DeletePage;
import com.apriori.cid.ui.utils.ButtonTypeEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class AssemblyGroupDeleteTests extends TestBaseUI {

    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;

    @AfterEach
    public void cleanUp() {
        if (componentAssembly != null) {
            new AssemblyUtils().deleteAssemblyAndComponents(componentAssembly);
        }
    }

    @Test
    @TestRail(id = {15023, 15024, 15025, 15026})
    @Description("Delete 10 subcomponents of an assembly")
    public void assemblyMenuGroupDelete() {

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

        componentAssembly = new AssemblyRequestUtil().getAssembly("RandomShapeAsm");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName());
        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isFalse();

        List<ComponentInfoBuilder> includedSubcomponents = componentAssembly.getSubComponents().stream().filter(o -> !o.getComponentName().equalsIgnoreCase(blob)).collect(Collectors.toList());

        includedSubcomponents.forEach(includedSubcomponent -> {
            componentsTreePage.multiSelectSubcomponents(includedSubcomponent + "," + componentAssembly.getScenarioName(),
                includedSubcomponent + "," + componentAssembly.getScenarioName(), includedSubcomponent + "," + componentAssembly.getScenarioName(),
                includedSubcomponent + "," + componentAssembly.getScenarioName(), includedSubcomponent + "," + componentAssembly.getScenarioName(),
                includedSubcomponent + "," + componentAssembly.getScenarioName(), includedSubcomponent + "," + componentAssembly.getScenarioName(),
                includedSubcomponent + "," + componentAssembly.getScenarioName(), includedSubcomponent + "," + componentAssembly.getScenarioName(),
                includedSubcomponent + "," + componentAssembly.getScenarioName());
        });

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isTrue();

        componentsTreePage.multiSelectSubcomponents(blob + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isFalse();

        componentsTreePage.multiSelectSubcomponents(blob + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.DELETE)).as("Delete Button Enabled").isTrue();

        componentsTreePage.deleteSubcomponent()
            .clickDelete(DeletePage.class)
            .clickClose(ComponentsTreePage.class)
            .checkComponentDeleted(componentAssembly, arc, cube50, ellipse, octagon, cube75, hexagon, cube100, slot, cuboid, cylinder)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        includedSubcomponents.forEach(includedSubcomponent ->
            softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(includedSubcomponent.getComponentName())).isEqualTo(true));

        softAssertions.assertAll();
    }
}
