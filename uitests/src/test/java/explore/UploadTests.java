package explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.FileOpenError;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {
    private CIDLoginPage loginPage;
    private FileOpenError fileOpenError;
    private ExplorePage explorePage;

    private File resourceFile;

    @Test
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
    @TestRail(testCaseId = {"576"})
    @Description("Nothing uploaded or translated if user select a file but then cancels the new component dialog")
    public void cancelUpload() {

        resourceFile = new FileResourceUtil().getResourceFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndCancel(testScenarioName, resourceFile, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "InvalidFileType"), is(equalTo(0)));
    }
}