package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupAssemblyTests extends TestBase {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private ComponentsListPage componentsListPage;
    private static UserCredentials currentUser;
    private static String scenarioName;

    public LargeGroupAssemblyTests() {
        super();
    }

    @BeforeClass
    public static void assemblySetup() {

        final String assemblyName = "Gym Bike";
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList("centre bolt", "centre washer", "display", "gasket", "Handle",
            "left paddle", "leg cover", "leg", "mechanism body", "paddle bar", "pin",
            "right paddle", "seat lock", "seat", "steer wheel support", "washer");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String subComponentExtension = ".ipt";

        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
    }

    @Test
    @TestRail(testCaseId = {"11807", "11804"})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void testPublishButtonAvailability() {

        final String CENTRE_BOLT = "centre bolt";
        final String CENTRE_WASHER = "centre washer";
        final String DISPLAY = "display";
        final String GASKET = "gasket";
        final String HANDLE = "Handle";
        final String LEFT_PADDLE = "left paddle";
        final String STEER_WHEEL_SUPPORT = "steer wheel support";
        final String SEAT_LOCK = "seat lock";
        final String MECHANISM_BODY = "mechanism body";
        final String PADDLE_BODY = "paddle bar";
        final String PIN = "pin";

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(
                CENTRE_BOLT + ", " + scenarioName + "",
                CENTRE_WASHER + ", " + scenarioName + "",
                DISPLAY + ", " + scenarioName + "",
                GASKET + ", " + scenarioName + "",
                HANDLE + ", " + scenarioName + "",
                LEFT_PADDLE + ", " + scenarioName + "",
                STEER_WHEEL_SUPPORT + ", " + scenarioName + "",
                SEAT_LOCK + ", " + scenarioName + "",
                MECHANISM_BODY + ", " + scenarioName + "",
                PADDLE_BODY + ", " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH), is(true));

        componentsListPage.multiSelectSubcomponents(PIN + ", " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH), is(false));
    }
}
