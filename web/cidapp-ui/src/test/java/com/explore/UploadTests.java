package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
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

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;

    @Test
    @TestRail(testCaseId = {"5422"})
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
    @TestRail(testCaseId = "10558")
    @Description("Successful creation of new scenario from existing scenario")
    public void testUploadAssemblyAndRenameScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String filename = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .createScenario()
            .enterScenarioName(newScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED), is(true));
        assertThat(evaluatePage.getCurrentScenarioName(), is(equalTo(newScenarioName)));
    }

    @Test
    @TestRail(testCaseId = {"5423"})
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
    @TestRail(testCaseId = {"6652"})
    @Description("Validate CAD association remains and attributes can be updated between CID sessions.")
    public void cadConnectionRemains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "225_gasket-1-solid1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cidComponentItem, EvaluatePage.class)
            .logout()
            .login(UserUtil.getUser())
            .selectFilter("Public")
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD), is(true));
    }
}