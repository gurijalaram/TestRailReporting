package compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class EditPublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"421"})
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    public void testPublishComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.findComparison(testComparisonName).isDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"427"})
    @Description("Test editing a published comparison shows the comparison view")
    public void testEditPublicComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.editScenario(ComparePage.class);

        assertThat(comparePage.isComparisonName(testComparisonName.toUpperCase()), is(true));
    }
}