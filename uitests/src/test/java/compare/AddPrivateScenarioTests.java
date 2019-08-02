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
import main.java.properties.reader.FileResourceReader;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class AddPrivateScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public AddPrivateScenarioTests() {
        super();
    }

    @Test
    @Description("Test filtering and adding a private scenario then searching component table for the scenario")
    @Severity(SeverityLevel.CRITICAL)
    public void filterAddPrivateScenario() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Standard Anneal", new FileResourceReader().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        explorePage.createNewComparison()
            .enterComparisonName("Private Comparison")
            .save()
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "PlasticMoulding")
            .apply(ComparisonTablePage.class);

        assertThat(new ComparisonTablePage(driver).findComparison("LeakTest", "PlasticMoulding").isDisplayed(), Matchers.is(true));
    }
}