package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentPrimaryPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupCostingTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private ComponentPrimaryPage componentPrimaryPage;
    private UserCredentials currentUser;
    private List<File> subComponents = new ArrayList<File>();
    private File assembly;

    public GroupCostingTests() {
        super();
    }

    private EvaluatePage uploadPartsThenAssembly(String[] subComponentNames, String assemblyName, String scenarioName, UserCredentials currentUser,
                                                 ProcessGroupEnum prtProcessGroupEnum, ProcessGroupEnum asmProcessGroupEnum) {
        for (int i=0; i<subComponentNames.length; i++) {
            subComponents.add(FileResourceUtil.getCloudFile(prtProcessGroupEnum, subComponentNames[i] + ".SLDPRT"));
        }
        assembly = FileResourceUtil.getCloudFile(asmProcessGroupEnum, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        for (int i=0; i<subComponents.size(); i++) {
            explorePage.uploadComponent(subComponentNames[i], scenarioName, subComponents.get(i), currentUser);
        }

        return explorePage.uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser);
    }

    @Test
    // @TestRail(testCaseId = {"11088", "11089"})
    @Description("Verify set inputs button only available for 10 or less sub-components")
    public void selectMaxTenSubComponentsTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String[] subComponentNames = {
            "50mmArc", "50mmCube", "50mmEllipse", "50mmOctagon", "75mmCube", "75mmHexagon",
            "100mmCube", "100mmSlot", "150mmCuboid", "200mmCylinder", "500mmBlob"};
        String assemblyName = "RandomShapeAsm";

//        for (int i=0; i<subComponentNames.length; i++) {
//            subComponents.add(FileResourceUtil.getCloudFile(processGroupEnum, subComponentNames[i] + ".SLDPRT"));
//        }
//        assembly = FileResourceUtil.getCloudFile(processGroupEnum, assemblyName + ".SLDASM");
//
//        loginPage = new CidAppLoginPage(driver);
//        explorePage = loginPage.login(currentUser);
//
//        for (int i=0; i<subComponents.size(); i++) {
//            explorePage.uploadComponent(subComponentNames[i], scenarioName, subComponents.get(i), currentUser);
//        }
//
//        evaluatePage = explorePage.uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser);

        evaluatePage = uploadPartsThenAssembly(subComponentNames, assemblyName, scenarioName, currentUser, processGroupEnum, processGroupEnum);
        componentsListPage = evaluatePage.openComponents();

        assertThat(componentsListPage.isSetInputsEnabled(), is(false));

        /* Verify that the set inputs button is enabled on first item selection and remains active up to 10 sub-components*/
        for (int i=0; i<subComponentNames.length-1; i++){
            componentsListPage.multiHighlightScenarios(subComponentNames[i].toUpperCase() +"," + scenarioName);
            assertThat(componentsListPage.isSetInputsEnabled(), is(true));
        }

        /* Verify that set inputs button becomes inactive when more than 10 sub-components selected */
        componentsListPage.highlightScenario(subComponentNames[subComponentNames.length-1].toUpperCase(), scenarioName);
        assertThat(componentsListPage.isSetInputsEnabled(), is(false));

        Random rand = new Random();
        componentsListPage.highlightScenario(subComponentNames[rand.nextInt(subComponentNames.length)].toUpperCase(), scenarioName);
        assertThat(componentsListPage.isSetInputsEnabled(), is(true));
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

        String[] subComponentNames = {"big ring", "small ring", "Pin"};
        String assemblyName = "Hinge assembly";

        evaluatePage = uploadPartsThenAssembly(subComponentNames, assemblyName, scenarioName, currentUser, prtProcessGroupEnum, asmProcessGroupEnum);
        componentsListPage = evaluatePage.openComponents();

        // Select each of the sub-components
        for (int i=0; i<subComponentNames.length; i++){
            componentsListPage.multiHighlightScenarios(subComponentNames[i].toUpperCase() +"," + scenarioName);
        }

        componentPrimaryPage = componentsListPage.setInputs();

        // Verify default text in Set Inputs Primary - If text is not as expected add to the error list
        List<String> errors = new ArrayList<String>();

        if (!componentPrimaryPage.getProcessGroup().equals(retainText)) {
            errors.add(componentPrimaryPage.getProcessGroup());
        }
        if (!componentPrimaryPage.getDigitalFactory().equals(retainText)) {
            errors.add(componentPrimaryPage.getDigitalFactory());
        }
        if (!componentPrimaryPage.getMaterial().equals(retainText)) {
            errors.add(componentPrimaryPage.getMaterial());
        }
        if (!componentPrimaryPage.getAnnualVolumePlaceholder().equals(retainText)) {
            errors.add(componentPrimaryPage.getAnnualVolumePlaceholder());
        }
        if (!componentPrimaryPage.getYearsPlaceholder().equals(retainText)) {
            errors.add(componentPrimaryPage.getYearsPlaceholder());
        }

        assertThat(errors, is(empty()));
    }
}
