package main.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;

import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.explore.FilterCriteriaPage;
import main.java.pages.login.LoginPage;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Scanner;

public class AddPublicScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private String filePath = new Scanner(AddPublicScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public AddPublicScenarioTests() {
        super();
    }

    /**
     * Test filtering and adding a public scenario then searching component table for the scenario
     */
    @Test
    public void filterAddPublicScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Standard Anneal", filePath, "Casting.prt")
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .publishScenario()
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName("Public Comparison")
            .save()
            .addScenario()
            .filterCriteria();

        new FilterCriteriaPage(driver).filterPublicCriteria("Part", "Part Name", "Contains", "HoleProximityTest")
            .apply();
        assertThat(new ComparisonTablePage(driver).findComparison("HoleProximityTest", "Initial").isDisplayed(), Matchers.is(true));
    }
}