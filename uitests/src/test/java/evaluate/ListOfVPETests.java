package evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateNameUtil;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class ListOfVPETests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ListOfVPETests() {
        super();
    }

    @Test
    @Description("Get List of VPEs")
    public void getVPEsList() {
        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile);

        assertThat(evaluatePage.getListOfVPEs(), hasItems(VPEEnum.getNames()));
    }
}