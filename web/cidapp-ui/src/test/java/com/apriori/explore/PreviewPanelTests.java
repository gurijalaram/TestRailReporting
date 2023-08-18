package com.apriori.explore;

import static com.apriori.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.explore.PreviewPage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PreviewPanelTests extends TestBaseUI {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private PreviewPage previewPage;
    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    private SoftAssertions softAssertions = new SoftAssertions();

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(id = {6350, 6349})
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
            .selectMaterial(MaterialNameEnum.ABS_10_GLASS.getMaterialName())
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
    @Tag(SMOKE)
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(id = {6351, 6201, 6352})
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
            .selectMaterial(MaterialNameEnum.ABS_10_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .highlightScenario(componentName, testScenarioName)
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.isImageDisplayed()).isEqualTo(true);
        softAssertions.assertThat(previewPage.getMaterialResult("Piece Part Cost")).as("Piece Part Cost").isCloseTo(Double.valueOf(0.48), Offset.offset(3.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Fully Burdened Cost")).as("Fully Burdened Cost").isCloseTo(Double.valueOf(0.86), Offset.offset(3.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Total Capital Investment")).as("Total Capital Investment").isCloseTo(Double.valueOf(11972.41), Offset.offset(50.00));

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Issue("APD-1663")
    @Description("Validate user can select multiple items with the checkboxes or all items on a page by checkbox on a top")
    @TestRail(id = {6202, 6203, 6204})
    public void previewPanelMultiSelect() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "Y_shape";
        String componentName3 = "Casting-Die";
        String componentName4 = "partbody_2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt");
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
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(componentName3, scenarioName3, resourceFile3, currentUser)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(componentName4, scenarioName4, resourceFile4, currentUser)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.getSelectionTitle()).isEqualTo("2 Scenarios Selected");

        previewPage.closePreviewPanel()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.NOTES, OperationEnum.CONTAINS, notes)
            .submit(ExplorePage.class)
            .selectAllScenarios()
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.getSelectionTitle()).isEqualTo("4 Scenarios Selected");

        softAssertions.assertAll();
    }
}