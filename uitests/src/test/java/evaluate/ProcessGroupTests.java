package test.java.evaluate;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class ProcessGroupTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;
    private String filePath = new Scanner(ProcessGroupTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public ProcessGroupTests() {
        super();
    }

    @Test
    @Description("Get List of Process Groups")
    @Severity(SeverityLevel.CRITICAL)
    public void getProcessGroupList() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("ProcessGroupList", filePath, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(ProcessGroupEnum.getNames()));
    }
}
