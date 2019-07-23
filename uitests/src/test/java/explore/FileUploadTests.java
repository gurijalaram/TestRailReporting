package test.java.explore;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class FileUploadTests extends TestBase {

    private LoginPage loginPage;
    private String filePath = new Scanner(FileUploadTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public FileUploadTests() {
        super();
    }

    @Test
    @Description("Test file upload")
    public void testFileUpload() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", filePath, "Casting.prt");
        //Assert.assertTrue();
    }
}