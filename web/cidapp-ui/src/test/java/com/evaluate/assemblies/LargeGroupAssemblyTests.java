package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class LargeGroupAssemblyTests extends TestBase {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private ComponentsListPage componentsListPage;
    private static UserCredentials currentUser;
    private static String scenarioName;
    private SoftAssertions softAssertions = new SoftAssertions();

    public LargeGroupAssemblyTests() {
        super();
    }

    @BeforeClass
    public static void assemblySetup() {
        int f = 1;
        int r = 2;

        if (f < r) {
            throw new RuntimeException("Exception caught");
        }
    }

    @Test
    @TestRail(testCaseId = {"11807", "11804"})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void testPublishButtonAvailability() {
        loginPage = new CidAppLoginPage(driver);
    }

    @Test
    @TestRail(testCaseId = {"11807", "11804"})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void test1() {
        loginPage = new CidAppLoginPage(driver);
    }

    @Test
    @TestRail(testCaseId = {"11807", "11804"})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void test2() {
        loginPage = new CidAppLoginPage(driver);
    }

    @Test
    @TestRail(testCaseId = {"11807", "11804"})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void test3() {
        loginPage = new CidAppLoginPage(driver);
    }
}
