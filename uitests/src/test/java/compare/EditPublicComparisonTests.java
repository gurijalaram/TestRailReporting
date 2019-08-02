package test.java.compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparePage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class EditPublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    @Severity(SeverityLevel.NORMAL)
    public void testEditPublicComparisonPublish() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateComparisonTests", new FileResourceUtil().getResourceFile("Casting.prt"))
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePrivateComparison10")
            .save();

        new EvaluatePage(driver).publishScenario();

        assertThat(new ExplorePage(driver).findComparison("DeletePrivateComparison10").isDisplayed(), is(true));
    }

    @Test
    @Description("Test editing a published comparison shows the comparison view")
    @Severity(SeverityLevel.NORMAL)
    public void testEditPublicComparison() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateComparisonTests", new FileResourceUtil().getResourceFile("Casting.prt"))
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePrivateComparison10")
            .save();

        new EvaluatePage(driver).publishScenario()
            .openComparison("DeletePrivateComparison10");

        assertThat(new ComparePage(driver).getDescriptionText(), containsString("DeletePrivateComparison10"));
    }
}
