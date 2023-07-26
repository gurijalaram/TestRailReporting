package com.evaluate.assemblies;

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

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

import java.util.Arrays;
import java.util.List;

public class LargeGroupAssemblyTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private ComponentsTreePage componentsTreePage;
    private static UserCredentials currentUser;
    private static String scenarioName;
    private SoftAssertions softAssertions = new SoftAssertions();

    public LargeGroupAssemblyTests() {
        super();
    }

    @BeforeClass
    public static void assemblySetup() {
        final String assemblyName = "Gym Bike";
        final String assemblyExtension = ".iam";
        List<String> subComponentNames = Arrays.asList(CENTRE_BOLT.getPartName(), CENTRE_WASHER.getPartName(), DISPLAY.getPartName(), GASKET.getPartName(), HANDLE.getPartName(),
            LEFT_PADDLE.getPartName(), LEG_COVER.getPartName(), LEG.getPartName(), MECHANISM_BODY.getPartName(), PADDLE_BAR.getPartName(), PIN.getPartName(),
            RIGHT_PADDLE.getPartName(), SEAT_LOCK.getPartName(), SEAT.getPartName(), STEER_WHEEL_SUPPORT.getPartName(), WASHER.getPartName());
        final String subComponentExtension = ".ipt";

        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;

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
    @Category(ExtendedRegression.class)
    @TestRail(id = {11807, 11804})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void testPublishButtonAvailability() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(
                CENTRE_BOLT.getPartName() + ", " + scenarioName + "",
                CENTRE_WASHER.getPartName() + ", " + scenarioName + "",
                DISPLAY.getPartName() + ", " + scenarioName + "",
                GASKET.getPartName() + ", " + scenarioName + "",
                HANDLE.getPartName() + ", " + scenarioName + "",
                LEFT_PADDLE.getPartName() + ", " + scenarioName + "",
                STEER_WHEEL_SUPPORT.getPartName() + ", " + scenarioName + "",
                SEAT_LOCK.getPartName() + ", " + scenarioName + "",
                MECHANISM_BODY.getPartName() + ", " + scenarioName + "",
                PADDLE_BAR.getPartName() + ", " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsTreePage.multiSelectSubcomponents(PIN.getPartName() + ", " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        softAssertions.assertAll();
    }
}
