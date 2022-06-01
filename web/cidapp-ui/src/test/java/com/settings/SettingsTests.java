package com.settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.pages.settings.SelectionPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColourEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SettingsTests extends TestBase {
    private File resourceFile;
    private CidAppLoginPage loginPage;
    private DisplayPreferencesPage displayPreferencesPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultsPage productionDefaultPage;
    private UserCredentials currentUser;
    private SelectionPage selectionPage;
    private ComponentInfoBuilder cidComponentItem;
    private SecondaryPage secondaryPage;
    private MaterialSelectorPage materialSelectorPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"6283"})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE)
            .openMaterialSelectorTable()
            .selectMaterial("ABS, Plating")
            .submit(ProductionDefaultsPage.class)
            .inputAnnualVolume("3000")
            .inputYears("7")
            .inputBatchSize("50")
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getScenarioName()).isEqualTo("MP Auto Test");
        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("ABS, Plating");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6281", "5442"})
    @Description("User can change the default Process group")
    public void defaultPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING)
            .submit(ExplorePage.class)
            .uploadComponent(componentName, testScenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6282"})
    @Description("User can change the default VPE")
    public void defaultVPE() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory()));
    }

    @Test
    @TestRail(testCaseId = {"6285", "6286"})
    @Description("User can change the default Production Life")
    public void defaultProductionLife() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputAnnualVolume("9524")
            .inputYears("7")
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getAnnualVolume()).isEqualTo("9524");
        softAssertions.assertThat(evaluatePage.getProductionLife()).isEqualTo("7");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6287", "6288"})
    @Description("User can change the default Batch size when set to manual")
    public void defaultBatchSize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Push Pin";
        final String batchSize = "46";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize(batchSize)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToSecondaryTab();

        assertThat(secondaryPage.getBatchSize(), is(equalTo(Integer.parseInt(batchSize))));
    }

    @Test
    @TestRail(testCaseId = {"6298"})
    @Description("User should be able to select a material catalogue from a different region than the VPE")
    public void differentMaterialCatalog() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_GERMANY)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6280"})
    @Description("User can change the default selection colour")
    public void defaultColor() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        selectionPage = loginPage.login(currentUser)
            .openSettings()
            .goToSelectionTab()
            .selectColour(ColourEnum.PEAR)
            .submit(ExplorePage.class)
            .openSettings()
            .goToSelectionTab();

        assertThat(selectionPage.isColour(ColourEnum.PEAR), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6284"})
    @Description("User can change the default Material")
    public void defaultMaterial() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .openMaterialSelectorTable()
            .selectMaterial("HIPS Extrusion")
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("HIPS Extrusion");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6279"})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_MEXICO)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005 Sponge")
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .logout()
            .login(currentUser)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.POWDER_METAL.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("F-0005 Sponge");

        softAssertions.assertAll();
    }

    @Ignore("feature has not yet been added for 21.1")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6289"})
    @Description("Manual Batch Quantity cannot be zero")
    public void batchSize0() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Ignore("feature has not yet been added for 21.1")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"3605"})
    @Description("Manual Batch Quantity cannot be junk")
    public void batchSizeJunk() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("JUNK");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Ignore("feature has not yet been added for 21.1")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6306", "6307"})
    @Description("Manual Batch Quantity cannot be a decimal")
    public void batchSizeDecimal() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0.12.00");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Test
    @TestRail(testCaseId = {"6308"})
    @Description("Changes made on all tabs of the user preferences should be saved regardless of the tab that the save button was closed on")
    public void saveAllTabs() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        displayPreferencesPage = loginPage.login(currentUser)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .setSystem("Imperial")
            .goToProductionTab()
            .inputScenarioName("Save all tabs test")
            .inputAnnualVolume("295")
            .inputYears("7")
            .goToSelectionTab()
            .selectColour(ColourEnum.AMBER)
            .submit(ExplorePage.class)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.isSystemChecked("Imperial")).isEqualTo(true);

        productionDefaultPage = displayPreferencesPage.goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getScenarioName()).isEqualTo("Save all tabs test");

        selectionPage = productionDefaultPage.goToSelectionTab();

        softAssertions.assertThat(selectionPage.isColour(ColourEnum.AMBER)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6300", "6304"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialSelectorPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_USA)
            .openMaterialSelectorTable();

        assertThat(materialSelectorPage.getListOfMaterials(), hasItems("F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }
}
