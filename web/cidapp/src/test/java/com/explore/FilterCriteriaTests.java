package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6213"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheetMetal.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .typeAheadFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "SheetMetal")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6214")
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .filter()
            .typeAheadFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Process Group", "is", ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Casting"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6215")
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .typeAheadFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Part Name", "Contains", "Wall")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "CurvedWall"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6216")
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .typeAheadFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "Piston_assembly")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6217")
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .addScenarioNotes()
            .typeAheadInSection("Status", "Analysis")
            .typeAheadInSection("Cost Maturity", "High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(EvaluatePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .typeAheadFilter("Public")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Status", "is", "Analysis")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6218"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .typeAheadFilter("Public")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "Push Pin")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6219")
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .addScenarioNotes()
            .typeAheadInSection("Status", "Complete")
            .typeAheadInSection("Cost Maturity", "High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(ExplorePage.class)
            .filter()
            .typeAheadFilter("Public")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Description", "Contains", "Test Description")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6221")
    @Description("Test public criteria assembly description")
    public void testFilterAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario()
            .typeAheadInSection("Status", "Analysis")
            .typeAheadInSection("Cost Maturity", "Initial")
            // TODO: 29/03/2021 ciene - assignee doesn't work at the moment. will be implemented in the future
            //.typeAheadInSection("Assignee", "Ciene Frith")
            .lock()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName("Automation")
            .typeAheadFilter("Status")
            .typeAheadFilter("In")
            .typeAheadFilter("Analysis")
            // TODO: 29/03/2021 ciene - the following steps require automation to set multiple rows.  these are commented for now until we have better locators
            //.typeAheadFilter("Cost Maturity")
            //.typeAheadFilter("In")
            //.typeAheadFilter("Initial")
            //.typeAheadFilter("Assignee")
            //.typeAheadFilter("In")
            //.typeAheadFilter("Ciene Frith")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "PowderMetalShaft"), is(equalTo(1)));
    }
}