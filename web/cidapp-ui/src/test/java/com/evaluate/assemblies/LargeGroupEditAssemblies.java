package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupEditAssemblies extends TestBase {

    private static String scenarioName;
    private static String scenarioName2;
    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;

    private static final String CENTRE_BOLT = "centre bolt";
    private static final String CENTRE_WASHER = "centre washer";
    private static final String DISPLAY = "display";
    private static final String GASKET = "gasket";
    private static final String HANDLE = "Handle";
    private static final String LEFT_PADDLE = "left paddle";
    private static final String STEER_WHEEL_SUPPORT = "steer wheel support";
    private static final String SEAT_LOCK = "seat lock";
    private static final String MECHANISM_BODY = "mechanism body";
    private static final String PADDLE_BAR = "paddle bar";
    private static final String LEG_COVER = "leg cover";
    private static final String LEG = "leg";
    private static final String RIGHT_PADDLE = "right paddle";
    private static final String WASHER = "washer";
    private static final String SEAT = "seat";
    private static final String PIN = "pin";

    @BeforeClass
    public static void setupAssembly() {
        final String assemblyName = "Gym Bike";
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList(CENTRE_BOLT, CENTRE_WASHER, DISPLAY, GASKET, HANDLE, LEFT_PADDLE, LEG_COVER, LEG, MECHANISM_BODY, PADDLE_BAR, PIN, RIGHT_PADDLE, SEAT_LOCK,
            SEAT, STEER_WHEEL_SUPPORT, WASHER);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final String subComponentExtension = ".ipt";

        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();
        scenarioName2 = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
    }

    @Test
    @TestRail(testCaseId = {"10883", "10884", "10885", "10894", "11140"})
    @Description("group Edit sub Components")
    public void editButtonUnavailable() {

        SoftAssertions softAssertions = new SoftAssertions();

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
                LEG_COVER + "," + scenarioName + "",
                LEG + "," + scenarioName + "",
                MECHANISM_BODY + "," + scenarioName + "",
                PADDLE_BAR + "," + scenarioName + "",
                PIN + "," + scenarioName + "");
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("centre bolt, " + scenarioName + "")
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(scenarioName2)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, CENTRE_WASHER + "," + DISPLAY + "," + GASKET + "," + HANDLE + "," + LEFT_PADDLE + "," + LEG_COVER + "," + LEG +
                "," + MECHANISM_BODY + "," + PADDLE_BAR + "," + PIN);

        softAssertions.assertThat(componentsListPage.getRowDetails(CENTRE_WASHER, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(DISPLAY, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(GASKET, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(HANDLE, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(LEFT_PADDLE, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(LEG_COVER, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(LEG, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(MECHANISM_BODY, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(PADDLE_BAR, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(PIN, scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(CENTRE_BOLT, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(SEAT, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(STEER_WHEEL_SUPPORT, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(WASHER, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }
}
