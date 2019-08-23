package test.java.explore;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.UsersEnum;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
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
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"));
    }
}