package com.apriori.cid.ui.tests.explore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.PreferencesEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UploadTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    private File resourceFile;
    private UserCredentials currentUser;
    private ComponentInfoBuilder assembly;

    @Test
    @TestRail(id = {5422, 12167})
    @Description("Failed upload of any other types of files")
    public void invalidFile() {
        resourceFile = FileResourceUtil.getResourceAsFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String fileError;

        loginPage = new CidAppLoginPage(driver);
        fileError = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(testScenarioName, resourceFile)
            .getAlertWarning();

        assertThat(fileError, containsString("The file type of the selected file is not supported"));
    }

    @Test
    @TestRail(id = 10558)
    @Description("Successful creation of new scenario from existing scenario")
    public void testUploadAssemblyAndRenameScenario() {
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

        assembly = new AssemblyRequestUtil().getAssembly();

        userPreferencesUtil.setSpecificPreference(assembly.getUser(), PreferencesEnum.ASSEMBLY_STRATEGY, "PREFER_PUBLIC");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(assembly.getUser())
            .uploadComponentAndOpen(assembly)
            .copyScenario()
            .enterScenarioName(newScenarioName)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(newScenarioName)).isTrue();

        userPreferencesUtil.resetSpecificPreference(assembly.getUser(), PreferencesEnum.ASSEMBLY_STRATEGY, "");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5423, 11892})
    @Description("Nothing uploaded or translated if user select a file but then cancels the new component dialog")
    public void cancelUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Piston_assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndCancel(testScenarioName, resourceFile, ExplorePage.class)
            .clickSearch(componentName);

        assertThat(explorePage.getListOfScenarios(componentName, testScenarioName), is(equalTo(0)));
    }

    @Test
    @TestRail(id = {6652})
    @Description("Validate CAD association remains and attributes can be updated between CID sessions.")
    public void cadConnectionRemains() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .logout()
            .login(UserUtil.getUser())
            .selectFilter("Public")
            .clickSearch(component.getComponentName())
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component.getComponentName(), component.getScenarioName());

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD), is(true));
    }

    @Test
    @TestRail(id = 5623)
    @Description("Validate a user cannot upload an assembly from a non supported CAD package")
    public void uploadUnsupportedCADFile() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "SC Plasma 009-005";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".f3d");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String fileError;

        loginPage = new CidAppLoginPage(driver);
        fileError = loginPage.login(currentUser)
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .getAlertWarning();

        assertThat(fileError, containsString("The file type of the selected file is not supported"));
    }

    @Test
    @TestRail(id = 5448)
    @Description("User can upload a file, after a failed file upload")
    public void uploadAfterFailedUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "ANKARA_SEHPA_SKETCHUP";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".skp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String fileError;

        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        fileError = loginPage.login(currentUser)
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .getAlertWarning();

        softAssertions.assertThat(fileError).contains("The file type of the selected file is not supported");

        evaluatePage = new ExplorePage(driver)
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).isEqualTo(component.getScenarioName());

        softAssertions.assertAll();
    }
}