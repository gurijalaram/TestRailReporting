package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class ChangeMaterialSelectionTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Additive Manufacturing, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestAdditive() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("BasicScenario_Additive.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum AlSi10Mg"), is(true));

            new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Duraform ProX GF")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Duraform ProX GF"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Bar & Tube Fab, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestBarandTubeFab() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("BasicScenario_BarAndTube.prt"))
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, AISI 316")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Stainless Steel, Stock, AISI 316"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestCasting() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("ISO JMB 800-1")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("ISO JMB 800-1"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Brass, Cast, Yellow 270")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Brass, Cast, Yellow 270"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Cast, UNS C28000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Copper, Cast, UNS C28000"), is(true));
    }

    /*@Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestComposites() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Cast, UNS C28000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Copper, Cast, UNS C28000"), is(true));
    }*/

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Forging, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestForging() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("BasicScenario_Forging.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Titanium, Ti-5Al-2.5Sn")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Titanium, Ti-5Al-2.5Sn"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Abs"), is(true));

        new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("PET 30% Glass")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("PET 30% Glass"), is(true));
    }
}
