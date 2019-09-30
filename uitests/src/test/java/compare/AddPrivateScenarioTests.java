package test.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
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

public class AddPrivateScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ComparisonTablePage comparisonTablePage;

    public AddPrivateScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"412", "1171"})
    @Description("Test filtering and adding a private scenario then searching component table for the scenario")
    public void filterAddPrivateScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        comparisonTablePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(new Util().getComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface")
            .apply(ComparisonTablePage.class);

        assertThat(comparisonTablePage.findScenario(testScenarioName, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface").isDisplayed(), Matchers.is(true));
    }
}