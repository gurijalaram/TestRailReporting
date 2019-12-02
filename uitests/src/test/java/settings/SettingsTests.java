package settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultPage;
import com.apriori.pageobjects.pages.settings.SelectionSettingsPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class SettingsTests extends TestBase {
    private LoginPage loginPage;
    private ExplorePage explorePage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultPage productionDefaultPage;
    private MoreInputsPage moreInputsPage;
    private SelectionSettingsPage selectionSettingsPage;
    private WarningPage warningPage;

    @After
    public void resetAllSettings() {
        new AfterTestUtil(driver).resetAllSettings();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1609", "276"})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .enterScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_EASTERN_EUROPE.getVpe())
            .selectMaterial("ABS, Plating")
            .enterAnnualVolume("3000")
            .enterProductionLife("7")
            .selectBatchManual()
            .enterBatchInput("50");
        new SettingsPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        productionDefaultPage = explorePage.openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getScenarioName("MP Auto Test"), is(true));
        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(VPEEnum.APRIORI_BRAZIL.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(VPEEnum.APRIORI_EASTERN_EUROPE.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("ABS, Plating"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"274", "1609", "1602"})
    @Description("User can change the default Process group")
    public void defaultPG() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup());

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .openScenarioLink(testScenarioName, "bracket_basic", "publish");

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), CoreMatchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"275"})
    @Description("User can change the default VPE")
    public void defaultVPE() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe());

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getSelectedVPE(VPEEnum.APRIORI_MEXICO.getVpe()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"278", "279", "561"})
    @Description("User can change the default Production Life")
    public void defaultProductionLife() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .enterAnnualVolume("9524")
            .enterProductionLife("7");

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getAnnualVolume("9,524"), Matchers.is(true));
        assertThat(evaluatePage.getProductionLife("7"), Matchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"280", "281"})
    @Description("User can change the default Batch size when set to manual")
    public void defaultBatchSize() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("46");

        settingsPage = new SettingsPage(driver);
        moreInputsPage = settingsPage.save(ExplorePage.class)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openMoreInputs();

        assertThat(moreInputsPage.getBatchSize("46"), Matchers.is(true));
        assertThat(moreInputsPage.isCadFileName("push pin.stp"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"293"})
    @Description("User should be able to select a material catalogue from a different region than the VPE")
    public void differentMaterialCatalog() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_GERMANY.getVpe());
        settingsPage = new SettingsPage(driver);
        productionDefaultPage = settingsPage.save(ExplorePage.class)
            .openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getSelectedVPE(VPEEnum.APRIORI_USA.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(VPEEnum.APRIORI_GERMANY.getVpe()), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1609"})
    @Description("User can change the default selection colour")
    public void defaultColor() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openSelectionTab()
            .setColour(ColourEnum.ELECTRIC_PURPLE.getColour());
        settingsPage = new SettingsPage(driver);
        selectionSettingsPage = settingsPage.save(ExplorePage.class)
            .openSettings()
            .openSelectionTab();

        assertThat(selectionSettingsPage.getColour(), is(equalTo(ColourEnum.ELECTRIC_PURPLE.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"277"})
    @Description("User can change the default Material")
    public void defaultMaterial() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_INDIA.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .selectMaterial("HIPS Extrusion");

        settingsPage = new SettingsPage(driver);
        productionDefaultPage = settingsPage.save(ExplorePage.class)
            .openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(VPEEnum.APRIORI_INDIA.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("HIPS Extrusion"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"271"})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {

        UserCredentials testUser = UserUtil.getUser();

        loginPage = new LoginPage(driver);
        loginPage.login(testUser)
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_INDIA.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_MEXICO.getVpe())
            .selectMaterial("F-0005 Sponge");
        new SettingsPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        loginPage = explorePage.openAdminDropdown()
            .selectLogOut();

        loginPage = new LoginPage(driver);
        productionDefaultPage = loginPage.login(testUser)
            .openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(VPEEnum.APRIORI_INDIA.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(VPEEnum.APRIORI_MEXICO.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("F-0005 Sponge"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"282"})
    @Description("Manual Batch Quantity cannot be zero")
    public void batchSize0() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("0");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"300"})
    @Description("Manual Batch Quantity cannot be junk")
    public void batchSizeJunk() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("JUNK");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"301", "302"})
    @Description("Manual Batch Quantity cannot be a decimal")
    public void batchSizeDecimal() {

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("0.12.00");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"303"})
    @Description("Changes made on all tabs of the user preferences should be saved regardless of the tab that the save button was closed on")
    public void saveAllTabs() {

        loginPage = new LoginPage(driver);
        productionDefaultPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .changeDisplayUnits(UnitsEnum.ENGLISH.getUnit())
            .openProdDefaultTab()
            .enterScenarioName("Save all tabs test")
            .enterAnnualVolume("295")
            .enterProductionLife("7");

        new SettingsPage(driver).openSelectionTab()
            .setColour(ColourEnum.SHAMROCK_GREEN.getColour());

        new SettingsPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        settingsPage = explorePage.openSettings();
        assertThat(settingsPage.getSelectedUnits(UnitsEnum.ENGLISH.getUnit()), is(true));

        productionDefaultPage = new SettingsPage(driver).openProdDefaultTab();
        assertThat(productionDefaultPage.getScenarioName("Save all tabs test"), is(true));

        selectionSettingsPage = new SettingsPage(driver).openSelectionTab();
        assertThat(selectionSettingsPage.getColour(), is(equalTo(ColourEnum.SHAMROCK_GREEN.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"295", "299"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new LoginPage(driver);
        productionDefaultPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectMaterialCatalog(VPEEnum.APRIORI_USA.getVpe());

        assertThat(productionDefaultPage.getListOfMaterials(), containsInAnyOrder("<No default specified>", "F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }
}

