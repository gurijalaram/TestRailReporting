package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class DeletePrivateComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;

    public DeletePrivateComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"433"})
    @Description("Test a private comparison can be deleted from the explore page")
    public void testDeletePrivateScenario() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .selectExploreButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "Machined Box AMERICAS")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison();

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"430"})
    @Description("Test a private comparison can be deleted from the comparison page")
    public void deletePrivateComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        genericHeader.delete().deleteComparison();

        explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }
}