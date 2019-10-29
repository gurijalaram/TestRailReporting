package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

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
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"));

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(ProcessGroupEnum.getNames()));
    }

    @Test
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
                .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Piston_assembly.stp"));

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(AssemblyProcessGroupEnum.getNames()));
    }
}