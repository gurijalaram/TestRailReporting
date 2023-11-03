package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PublishTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private PublishPage publishPage;
    private File resourceFile;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();

    public PublishTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @Tag(SANITY)
    @Description("Publish a new scenario from the Private Workspace to the Public Workspace")
    @TestRail(id = {6729, 6731})
    public void testPublishNewCostedScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(greaterThan(0)));
    }

    @Test
    @TestRail(id = {6743, 6744, 6745, 6747, 6041, 21550})
    @Description("Publish a part and add an assignee, cost maturity and status")
    public void testPublishWithStatus() {
        final String file = "testpart-4.prt";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        publishPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class);

        softAssertions.assertThat(publishPage.getAssociationAlert()).contains("High maturity and complete status scenarios can be prioritized to make more accurate associations when uploading new assemblies.");

        publishPage.selectStatus("Analysis")
            .selectCostMaturity("Low")
            .selectAssignee(currentUser);

        explorePage = publishPage.publish(cidComponentItem, EvaluatePage.class).clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, scenarioName)
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioName)).isGreaterThan(0);

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}