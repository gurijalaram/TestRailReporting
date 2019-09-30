package test.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.hamcrest.Matchers;
import org.junit.Test;

public class AddPublicScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ComparisonTablePage comparisonTablePage;
    private WarningPage warningPage;

    public AddPublicScenarioTests() {
        super();
    }

    @Test
    @Description("Test filtering and adding a public scenario then searching component table for the scenario")
    public void filterAddPublicScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        comparisonTablePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .createNewComparison()
            .enterComparisonName(new Util().getComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Casting")
            .apply(ComparisonTablePage.class);

        assertThat(comparisonTablePage.findScenario(testScenarioName, "Casting").isDisplayed(), Matchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"462"})
    @Description("Test warning message appears when the user does not enter a scenario name for a comparison")
    public void comparisonNoScenarioName() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .createNewComparison()
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), is(containsString("Some of the supplied inputs are invalid.")));
    }
}