package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
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

public class PublishExistingCostedTests extends TestBaseUI {
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private SoftAssertions softAssertions = new SoftAssertions();

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6209, 5427, 6732})
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String filterName = generateStringUtil.generateFilterName();

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
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .openScenario(componentName, scenarioName)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .costScenario()
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, componentName)
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioName)).isGreaterThan(0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6211, 6734, 6040})
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).selectFilter("Private")
            .enterKeySearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Material Stock / Band Saw / Preheat / Hammer / Trim");

        softAssertions.assertAll();
    }
}