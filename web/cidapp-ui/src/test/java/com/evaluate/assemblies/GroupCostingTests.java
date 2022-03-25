package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentPrimaryPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GroupCostingTests extends TestBase {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentsListPage componentsListPage;
    private ComponentPrimaryPage componentPrimaryPage;
    private UserCredentials currentUser;
    private List<File> subComponents = new ArrayList<File>();

    public GroupCostingTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"11088", "11089"})
    @Description("Verify set inputs button only available for 10 or less sub-components")
    public void selectMaxTenSubComponentsTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final List<String> subComponentNames = Arrays.asList(
            "50mmArc", "50mmCube", "50mmEllipse", "50mmOctagon", "75mmCube", "75mmHexagon",
            "100mmCube", "100mmSlot", "150mmCuboid", "200mmCylinder", "500mmBlob");
        String subComponentExtension = ".SLDPRT";
        String assemblyName = "RandomShapeAsm";
        String assemblyExtension = ".SLDASM";

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

        componentsListPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .selectFilter("Recent")
            .openScenario(assemblyName, scenarioName)
            .openComponents();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(componentsListPage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        for (int i=0; i<subComponentNames.size()-1; i++){
            componentsListPage.selectScenario(subComponentNames.get(i).toUpperCase(), scenarioName);
            softAssertions.assertThat(componentsListPage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();
        }

        componentsListPage.selectScenario(subComponentNames.get(subComponentNames.size()-1).toUpperCase(), scenarioName);
        softAssertions.assertThat(componentsListPage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        Random rand = new Random();
        componentsListPage.selectScenario(subComponentNames.get(rand.nextInt(subComponentNames.size())).toUpperCase(), scenarioName);
        softAssertions.assertThat(componentsListPage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11097", "11090", "11092"})
    @Description("Verify sub-components are group costed successfully.")
    public void groupCostSubComponentsTest() {
        final String retainText = "Retain Existing Input";
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.FORGING;
        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final List<String> subComponentNames = Arrays.asList("big ring", "small ring", "Pin");
        String subComponentExtension = ".SLDPRT";
        String assemblyName = "Hinge assembly";
        String assemblyExtension = ".SLDASM";

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames,
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentsListPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .selectFilter("Recent")
            .openScenario(assemblyName, scenarioName)
            .openComponents();

        SoftAssertions softAssertions = new SoftAssertions();
        subComponentNames.forEach(subComponentName->componentsListPage.selectScenario(subComponentName.toUpperCase(), scenarioName));

        componentPrimaryPage = componentsListPage.setInputs();

        softAssertions.assertThat(componentPrimaryPage.getProcessGroup()).as("Process Group Text").isEqualTo(retainText);
        softAssertions.assertThat(componentPrimaryPage.getDigitalFactory()).as("Digital Factory Text").isEqualTo(retainText);
        //softAssertions.assertThat(componentPrimaryPage.getMaterial()).as("Material Text").isEqualTo(retainText);
        softAssertions.assertThat(componentPrimaryPage.getAnnualVolumePlaceholder()).as("Annual Volume Text").isEqualTo(retainText);
        softAssertions.assertThat(componentPrimaryPage.getYearsPlaceholder()).as("Years Text").isEqualTo(retainText);

        softAssertions.assertAll();
    }
}
