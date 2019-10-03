package explore;

import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class FileUploadTests extends TestBase {

    private LoginPage loginPage;

    public FileUploadTests() {
        super();
    }

    @Test
    @Description("Test file upload")
    public void testFileUpload() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"));
    }
}