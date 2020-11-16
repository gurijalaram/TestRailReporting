package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class ListProcessGroupTests extends TestBase {

    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ListProcessGroupTests() {
        super();
    }

    @Test
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(ProcessGroupEnum.getNames()));
    }

    @Test
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Piston_assembly.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(AssemblyProcessGroupEnum.getNames()));
    }
}