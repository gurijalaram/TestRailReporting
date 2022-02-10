package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.css.entity.response.Item;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private UserCredentials currentUser;
    private Item cssItem;
    private Item cssItemB;
    private Item cssItemC;
    private File subComponentA;
    private File subComponentB;
    private File subComponentC;
    private File assembly;

    public PublishAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10763", "10768"})
    @Description("Publish an assembly with no missing sub-components")
    public void shallowPublishAssemblyTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "titan battery release";
        String subComponentBName = "titan battery";
        String assemblyName = "titan battery ass";

        subComponentA = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, subComponentAName + ".SLDPRT");
        subComponentB = FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, subComponentBName + ".SLDPRT");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(subComponentAName, scenarioName, subComponentA, currentUser);

        cssItemB = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(subComponentBName, scenarioName, subComponentB, currentUser);

        cssItemC = new ExplorePage(driver).navigateToScenario(cssItemB)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .costScenario(3)
            .publishScenario()
            .publish(cssItemB, currentUser, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(assemblyName, scenarioName, assembly, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cssItemC)
            .selectProcessGroup(ASSEMBLY)
            .costScenario()
            .publishScenario()
            .publish(cssItemC, currentUser, EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }
}
