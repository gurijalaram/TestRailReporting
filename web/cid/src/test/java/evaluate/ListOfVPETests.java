package evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.login.CidLoginPage;

import java.io.File;

public class ListOfVPETests extends TestBase {

    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ListOfVPETests() {
        super();
    }

    @Test
    @Description("Get List of VPEs")
    public void getVPEsList() {
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.getListOfVPEs(), hasItems(VPEEnum.getNames()));
    }
}