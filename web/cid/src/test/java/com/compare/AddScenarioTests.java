package com.compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.GenericHeader;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class AddScenarioTests extends TestBase {

    private CidLoginPage loginPage;
    private WarningPage warningPage;
    private ComparePage comparePage;
    private ScenarioTablePage scenarioTablePage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private ExplorePage explorePage;

    private File resourceFile;

    @Test
    @Issue("AP-61539")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3847", "412", "1171"})
    @Description("Test filtering and adding a private scenario then searching component table for the scenario")
    public void filterAddPrivateScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        scenarioTablePage = evaluatePage.createNewComparison().enterComparisonName(new GenerateStringUtil().generateComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface")
            .apply(ScenarioTablePage.class);

        assertThat(scenarioTablePage.findScenario(testScenarioName, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface").isDisplayed(), Matchers.is(true));
    }

    @Test
    @Issue("AP-61539")
    @TestRail(testCaseId = {"448"})
    @Description("Test filtering and adding a public scenario then searching component table for the scenario")
    public void filterAddPublicScenario() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);

        scenarioTablePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(new GenerateStringUtil().generateComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class);

        assertThat(scenarioTablePage.findScenario(testScenarioName, "Casting").isDisplayed(), Matchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"462"})
    @Description("Test warning message appears when the user does not enter a scenario name for a comparison")
    public void comparisonNoScenarioName() {

        loginPage = new CidLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"414"})
    @Description("Test all available characters in a comparison name")
    public void comparisonAllCharacters() {

        String testComparisonName = (new GenerateStringUtil().generateComparisonName() + "!£$%^&()_+{}~`1-=[]#';@");

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"415"})
    @Description("Enter a very long comparison name and a very long description when creating a comparison")
    public void comparisonLongNameAndDescription() {

        String testComparisonName = (new GenerateStringUtil().generateComparisonName() + "abcdefghijklmopqrstuvwxyzabcdefghijklmopqrstuvwxyz");
        String testComparisonDescription = ("This is a very long description abcdefghijklmopqrstuvwxyzabcdefghijklmopqrstuvwxyz");

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .openPreviewPanel(ExplorePage.class);

        assertThat(explorePage.getDescriptionText(), is(equalTo(testComparisonDescription)));

        new ScenarioTablePage(driver).openComparison(testComparisonName);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"416"})
    @Description("User can create a new scenario of an existing comparison with a unique name")
    public void createNewScenarioOfExistingComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .createNewScenario()
                .enterScenarioName(testScenarioName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", testScenarioName)
                .apply(ScenarioTablePage.class)
                .openComparison(testComparisonName);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"417", "418"})
    @Description("User can create a new scenario of an existing comparison using all available characters, with a very long name")
    public void newCompScenarioAllCharactersLongName() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testScenarioName = (new GenerateStringUtil().generateScenarioName() + "This is very long name of scenario" + "!£$%^&()_+{}~`1-=[]#';@");

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .createNewScenario()
                .enterScenarioName(testScenarioName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", testScenarioName)
                .apply(ScenarioTablePage.class)
                .openComparison(testComparisonName);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"441"})
    @Description("In comparison view, user can access any scenario included in the comparison")
    public void accessScenarioIncludedInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        evaluatePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .openComparison(testComparisonName)
                .openScenarioFromComparison(partName, scenarioName);

        assertThat(evaluatePage.getCurrentScenarioName(scenarioName), is(true));

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        evaluatePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .apply(ExplorePage.class)
                .openComparison(testComparisonName)
                .openScenarioFromComparison(partName, scenarioName);

        assertThat(evaluatePage.getCurrentScenarioName(scenarioName), is(true));
    }
}
