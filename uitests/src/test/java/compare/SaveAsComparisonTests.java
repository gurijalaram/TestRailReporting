package compare;

import static org.hamcrest.CoreMatchers.containsString;
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
import io.qameta.allure.Issue;
import org.junit.Test;


public class SaveAsComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;

public SaveAsComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"419"})
    @Description("Test a private comparison can be have Save As performed on it")
    public void testSaveAsPrivateComparison() {

        String testComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonName = "Save As Comparison Name";

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.saveAs()


    }

}
