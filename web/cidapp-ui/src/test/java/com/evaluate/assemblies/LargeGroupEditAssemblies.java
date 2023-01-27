package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static com.utils.PartNamesEnum.CENTRE_BOLT;
import static com.utils.PartNamesEnum.CENTRE_WASHER;
import static com.utils.PartNamesEnum.DISPLAY;
import static com.utils.PartNamesEnum.GASKET;
import static com.utils.PartNamesEnum.HANDLE;
import static com.utils.PartNamesEnum.LEFT_PADDLE;
import static com.utils.PartNamesEnum.LEG;
import static com.utils.PartNamesEnum.LEG_COVER;
import static com.utils.PartNamesEnum.MECHANISM_BODY;
import static com.utils.PartNamesEnum.PADDLE_BAR;
import static com.utils.PartNamesEnum.PIN;
import static com.utils.PartNamesEnum.RIGHT_PADDLE;
import static com.utils.PartNamesEnum.SEAT;
import static com.utils.PartNamesEnum.SEAT_LOCK;
import static com.utils.PartNamesEnum.STEER_WHEEL_SUPPORT;
import static com.utils.PartNamesEnum.WASHER;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
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
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupEditAssemblies extends TestBase {

    private static String scenarioName;
    private static String scenarioName2;
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupAssembly() {
        final String assemblyName = "Gym Bike";
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList(CENTRE_BOLT.getPartName(), CENTRE_WASHER.getPartName(), DISPLAY.getPartName(), GASKET.getPartName(), HANDLE.getPartName(), LEFT_PADDLE.getPartName(),
            LEG_COVER.getPartName(), LEG.getPartName(), MECHANISM_BODY.getPartName(), PADDLE_BAR.getPartName(), PIN.getPartName(), RIGHT_PADDLE.getPartName(), SEAT_LOCK.getPartName(), SEAT.getPartName(),
            STEER_WHEEL_SUPPORT.getPartName(), WASHER.getPartName());
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
        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(
                CENTRE_BOLT.getPartName() + ", " + scenarioName + "",
                CENTRE_WASHER.getPartName() + ", " + scenarioName + "",
                DISPLAY.getPartName() + ", " + scenarioName + "",
                GASKET.getPartName() + ", " + scenarioName + "",
                HANDLE.getPartName() + ", " + scenarioName + "",
                LEFT_PADDLE.getPartName() + ", " + scenarioName + "",
                LEG_COVER.getPartName() + "," + scenarioName + "",
                LEG.getPartName() + "," + scenarioName + "",
                MECHANISM_BODY.getPartName() + "," + scenarioName + "",
                PADDLE_BAR.getPartName() + "," + scenarioName + "",
                PIN.getPartName() + "," + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents("centre bolt, " + scenarioName + "")
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(scenarioName2)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, CENTRE_WASHER.getPartName() + "," + DISPLAY.getPartName() + "," + GASKET.getPartName() + "," + HANDLE.getPartName() + "," +
                LEFT_PADDLE.getPartName() + "," + LEG_COVER.getPartName() + "," + LEG.getPartName() + "," + MECHANISM_BODY.getPartName() + "," + PADDLE_BAR.getPartName() + "," + PIN.getPartName());

        softAssertions.assertThat(componentsTablePage.getRowDetails(CENTRE_WASHER.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(DISPLAY.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(GASKET.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(HANDLE.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(LEFT_PADDLE.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(LEG_COVER.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(LEG.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(MECHANISM_BODY.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(PADDLE_BAR.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(PIN.getPartName(), scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(CENTRE_BOLT.getPartName(), scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(SEAT.getPartName(), scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(STEER_WHEEL_SUPPORT.getPartName(), scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(WASHER.getPartName(), scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }
}
