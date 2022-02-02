package com.explore;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.utils.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PreviewPanelTests extends TestBase {

    private CidAppLoginPage loginPage;
    private PreviewPage previewPage;

    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    UserCredentials currentUser;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(testCaseId = {"6350", "6349"})
    public void testPreviewPanelDisplay() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String partName = "Casting";
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(partName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .search("ABS, 10")
            .selectMaterial("ABS, 10% Glass")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(partName, testScenarioName)
            .openPreviewPanel();

        assertThat(previewPage.isPreviewPanelDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(testCaseId = {"6351", "6201", "6352"})
    public void previewPanelMetrics() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "225_gasket-1-solid1";
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("ABS, 10")
            .selectMaterial("ABS, 10% Glass")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .highlightScenario(componentName, testScenarioName)
            .openPreviewPanel();

        assertThat(previewPage.isImageDisplayed(), is(true));
        assertThat(previewPage.getMaterialResult("Piece Part Cost"), closeTo(0.48, 1));
        assertThat(previewPage.getMaterialResult("Fully Burdened Cost"), closeTo(0.86, 1));
        assertThat(previewPage.getMaterialResult("Total Capital Investment"), closeTo(10514.67, 10));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate user can select multiple items with the checkboxes or all items on a page by checkbox on a top")
    @TestRail(testCaseId = {"6202", "6203", "6204"})
    public void previewPanelMultiSelect() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "manifold";
        String componentName3 = "Casting-Die";
        String componentName4 = "partbody_2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
        resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum, componentName3 + ".stp");
        resourceFile4 = FileResourceUtil.getCloudFile(processGroupEnum, componentName4 + ".stp");
        currentUser = UserUtil.getUser();

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String scenarioName4 = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        String notes = new GenerateStringUtil().generateNotes();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .info()
                .inputNotes(notes)
                .submit(EvaluatePage.class)
                .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
                .info()
                .inputNotes(notes)
                .submit(EvaluatePage.class)
                .uploadComponentAndOpen(componentName3, scenarioName3, resourceFile3, currentUser)
                .info()
                .inputNotes(notes)
                .submit(EvaluatePage.class)
                .uploadComponentAndOpen(componentName4, scenarioName4, resourceFile4, currentUser)
                .info()
                .inputNotes(notes)
                .submit(EvaluatePage.class)
                .clickExplore()
                .selectFilter("Recent")
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
                .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
                .openPreviewPanel();

        assertThat(previewPage.getSelectionTitle(), is(equalTo("2 Scenarios Selected")));

        previewPage.closePreviewPanel()
                .filter()
                .saveAs()
                .inputName(filterName)
                .addCriteria(PropertyEnum.NOTES, OperationEnum.CONTAINS, notes)
                .submit(ExplorePage.class)
                .selectAllScenarios()
                .openPreviewPanel();

        assertThat(previewPage.getSelectionTitle(), is(equalTo("4 Scenarios Selected")));
    }
}
