package main.java.explore;

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
    public void testFileUpload() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .uploadFile("Scenario A", filePath, "testpart-4.prt");
        //        Assert.assertTrue();
    }
}
