package test.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparePage;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.time.LocalDateTime;

public class AddPrivateScenarioTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ComparisonTablePage comparisonTablePage;
    private ComparePage comparePage;

    public AddPrivateScenarioTests() {
        super();
    }

    @Test
    @Description("C412 Test filtering and adding a private scenario then searching component table for the scenario")
    @Severity(SeverityLevel.CRITICAL)
    public void filterAddPrivateScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparisonTablePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName("Private Comparison")
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface")
            .apply(ComparisonTablePage.class);

        assertThat(comparisonTablePage.findComparison(testScenarioName, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface").isDisplayed(), Matchers.is(true));
    }

    @Test
    @Description("C387 Testing the user can update comparisons")
    @Severity(SeverityLevel.CRITICAL)
    public void updatePrivateComparison() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .createNewScenario()
            .enterScenarioName("Scenario 2")
            .save()
            .createNewComparison()
            .enterComparisonName("Update Private Comparison")
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "partbody_2")
            .apply(ComparisonTablePage.class)
            .selectComparison(testScenarioName,"partbody_2")
            .apply()
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "partbody_2")
            .apply(ComparisonTablePage.class)
            .selectComparison("Scenario 2","partbody_2")
            .apply();


        //assertThat(comparisonTablePage.findComparison(testScenarioName, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface").isDisplayed(), Matchers.is(true));
    }

}