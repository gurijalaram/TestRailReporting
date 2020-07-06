package explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.explore.AssignPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.FileOpenError;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {
    private CIDLoginPage loginPage;
    private FileOpenError fileOpenError;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private AssignPage assignPage;
    private WarningPage warningPage;

    private File resourceFile;

    @Test
    @Issue("AP-61858")
    @TestRail(testCaseId = {"575"})
    @Description("Failed upload of any other types of files")
    public void invalidFile() {

        resourceFile = new FileResourceUtil().getResourceFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, FileOpenError.class);

        assertThat(fileOpenError.getErrorText(), containsString("The selected file type is not supported"));
    }

    @Test
    @Issue("AP-61858")
    @TestRail(testCaseId = {"575"})
    @Description("Failed upload of any other types of files")
    public void cancelUpload() {

        resourceFile = new FileResourceUtil().getResourceFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndCancel(testScenarioName, resourceFile, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "InvalidFileType"), is(equalTo(0)));
    }
}