package com.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;
import utils.Constants;

import java.io.File;

public class ListOfDigitalFactoryTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ListOfDigitalFactoryTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5415"})
    @Description("Get List of Digital Factory")
    public void getDigitalFactoryList() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(Constants.getUserCredentials())
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile);

        assertThat(evaluatePage.getListOfDigitalFactory(), hasItems(DigitalFactoryEnum.getNames()));
    }
}