package main.java.explore;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class FileUploadTests extends TestBase {

    private LoginPage loginPage;

    public FileUploadTests() {
        super();
    }

    @Test
    public void testFileUpload() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .uploadFile("Scenario A", "\\\\share.apriori.com\\common\\Departments\\Engineering\\QA\\Automation\\CID\\QAE\\parts\\Machining\\Milling\\3 Axis Mill\\", "testpart-4.prt");
        //        Assert.assertTrue();
    }
}
