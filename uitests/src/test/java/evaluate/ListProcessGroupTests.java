package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.Util;
import org.junit.Test;

public class ListProcessGroupTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ListProcessGroupTests() {
        super();
    }

    @Test
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"));

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(ProcessGroupEnum.getNames()));
    }
}