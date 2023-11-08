package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.cid.ui.utils.PartNamesEnum.CENTRE_BOLT;
import static com.apriori.cid.ui.utils.PartNamesEnum.CENTRE_WASHER;
import static com.apriori.cid.ui.utils.PartNamesEnum.DISPLAY;
import static com.apriori.cid.ui.utils.PartNamesEnum.GASKET;
import static com.apriori.cid.ui.utils.PartNamesEnum.HANDLE;
import static com.apriori.cid.ui.utils.PartNamesEnum.LEFT_PADDLE;
import static com.apriori.cid.ui.utils.PartNamesEnum.LEG;
import static com.apriori.cid.ui.utils.PartNamesEnum.LEG_COVER;
import static com.apriori.cid.ui.utils.PartNamesEnum.MECHANISM_BODY;
import static com.apriori.cid.ui.utils.PartNamesEnum.PADDLE_BAR;
import static com.apriori.cid.ui.utils.PartNamesEnum.PIN;
import static com.apriori.cid.ui.utils.PartNamesEnum.SEAT;
import static com.apriori.cid.ui.utils.PartNamesEnum.STEER_WHEEL_SUPPORT;
import static com.apriori.cid.ui.utils.PartNamesEnum.WASHER;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.EditComponentsPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dto.AssemblyDTORequest;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class LargeGroupEditAssemblies extends TestBaseUI {

    private static String scenarioName;
    private static String scenarioName2;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {10883, 10884, 10885, 10894, 11140})
    @Description("group Edit sub Components")
    public void editButtonUnavailable() {
        componentAssembly = new AssemblyDTORequest().getAssembly("Gym Bike");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView();

        componentsTablePage.multiSelectSubcomponents(CENTRE_BOLT.getPartName() + ", " + scenarioName,
            CENTRE_WASHER.getPartName() + ", " + scenarioName,
            DISPLAY.getPartName() + ", " + scenarioName,
            GASKET.getPartName() + ", " + scenarioName,
            HANDLE.getPartName() + ", " + scenarioName,
            LEFT_PADDLE.getPartName() + ", " + scenarioName,
            LEG_COVER.getPartName() + "," + scenarioName,
            LEG.getPartName() + "," + scenarioName,
            MECHANISM_BODY.getPartName() + "," + scenarioName,
            PADDLE_BAR.getPartName() + "," + scenarioName,
            PIN.getPartName() + "," + scenarioName);

        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents(CENTRE_BOLT.getPartName() + ", " + scenarioName)
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
