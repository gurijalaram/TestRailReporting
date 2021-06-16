package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class ListProcessGroupTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    UserCredentials currentUser;

    public ListProcessGroupTests() {
        super();
    }

    @Test
    @Issue("BA-1877")
    @TestRail(testCaseId = {"6197"})
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();


        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser);

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(ProcessGroupEnum.getNames()));
    }

    @Ignore("Assemblies cannot be upload")
    @Test
    @TestRail(testCaseId = {"6198"})
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Piston_assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser);

        assertThat(evaluatePage.getListOfProcessGroups(), hasItems(AssemblyProcessGroupEnum.getNames()));
    }
}