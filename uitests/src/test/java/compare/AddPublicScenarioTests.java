package test.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.hamcrest.Matchers;
import org.junit.Test;

public class AddPublicScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public AddPublicScenarioTests() {
        super();
    }

    @Test
    @Description("Test filtering and adding a public scenario then searching component table for the scenario")
    @Severity(SeverityLevel.CRITICAL)
    public void filterAddPublicScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Standard Anneal", new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("Public Comparison")
            .save()
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "HoleProximityTest")
            .apply(ComparisonTablePage.class);

        assertThat(new ComparisonTablePage(driver).findComparison("Initial", "HoleProximityTest").isDisplayed(), Matchers.is(true));
    }
}