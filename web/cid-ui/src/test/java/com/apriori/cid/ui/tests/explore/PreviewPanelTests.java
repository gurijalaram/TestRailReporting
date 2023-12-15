package com.apriori.cid.ui.tests.explore;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.PreviewPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PreviewPanelTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private PreviewPage previewPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(id = {6350, 6349})
    public void testPreviewPanelDisplay() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ABS, 10")
            .selectMaterial(MaterialNameEnum.ABS_10_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .openPreviewPanel();

        assertThat(previewPage.isPreviewPanelDisplayed(), is(true));
    }

    @Test
    @Tag(SMOKE)
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(id = {6351, 6201, 6352})
    public void previewPanelMetrics() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
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
            .clickSearch(component.getComponentName())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.isImageDisplayed()).isEqualTo(true);
        softAssertions.assertThat(previewPage.getMaterialResult("Piece Part Cost")).as("Piece Part Cost").isCloseTo(Double.valueOf(0.48), Offset.offset(3.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Fully Burdened Cost")).as("Fully Burdened Cost").isCloseTo(Double.valueOf(0.86), Offset.offset(3.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Total Capital Investment")).as("Total Capital Investment").isCloseTo(Double.valueOf(12256.87), Offset.offset(50.00));

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Issue("APD-1663")
    @Description("Validate user can select multiple items with the checkboxes or all items on a page by checkbox on a top")
    @TestRail(id = {6202, 6203, 6204})
    public void previewPanelMultiSelect() {
        String filterName = new GenerateStringUtil().generateFilterName();
        String notes = new GenerateStringUtil().generateNotes();

        component = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder component2 = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder component3 = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder component4 = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(component2)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(component3)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(component4)
            .clickActions()
            .info()
            .inputNotes(notes)
            .submit(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
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
