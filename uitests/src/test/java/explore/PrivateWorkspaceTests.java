package explore;

import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.Util;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class PrivateWorkspaceTests extends TestBase {

    private LoginPage loginPage;

    public PrivateWorkspaceTests() {
        super();
    }

    @Test
    @Description("Test opening scenario from private workspace")
    public void testSearchOpenScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(new Util().getScenarioName(), "Casting");
    }
}