package evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class ListOfVPETests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ListOfVPETests() {
        super();
    }

    @Test
    @Description("Get List of VPEs")
    public void getVPEsList() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"));

        assertThat(evaluatePage.getListOfVPEs(), hasItems(VPEEnum.getNames()));
    }
}