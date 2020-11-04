package explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.login.CidAppLoginPage;
import pageobjects.navtoolbars.EvaluateToolbar;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class UploadComponentTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluateToolbar evaluateToolbar;

    @Test
    @Category(SmokeTests.class)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluateToolbar = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluateToolbar.class);

        assertThat(evaluateToolbar.isCostLabel(NewCostingLabelEnum.CREATED.getCostingText()), is(true));
    }
}
