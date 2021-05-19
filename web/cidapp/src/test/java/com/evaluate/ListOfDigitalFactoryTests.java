package com.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.entity.response.css.Item;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;
import utils.CidAppTestUtil;

import java.io.File;

public class ListOfDigitalFactoryTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private UserCredentials currentUser;

    public ListOfDigitalFactoryTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5415"})
    @Description("Get List of VPEs")
    public void getVPEsList() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        currentUser = UserUtil.getUser();
        String componentName = "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,componentName + ".CATPart");

        Item component = new CidAppTestUtil().postComponents(componentName, scenarioName, resourceFile);
        String componentId = component.getComponentIdentity();
        String scenarioId = component.getScenarioIdentity();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentId, scenarioId);

        assertThat(evaluatePage.getListOfDigitalFactory(), hasItems(VPEEnum.getNames()));
    }
}