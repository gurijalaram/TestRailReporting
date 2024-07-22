package com.apriori.cid.ui.tests.settings;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.compare.ComparePage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.MaterialSelectorPage;
import com.apriori.cid.ui.pageobjects.evaluate.UpdateCadFilePage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.StockPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.ImportCadFilePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.settings.AssemblyDefaultsPage;
import com.apriori.cid.ui.pageobjects.settings.DisplayPreferencesPage;
import com.apriori.cid.ui.pageobjects.settings.ProductionDefaultsPage;
import com.apriori.cid.ui.pageobjects.settings.SelectionPage;
import com.apriori.cid.ui.utils.ColourEnum;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.ComparisonCardEnum;
import com.apriori.cid.ui.utils.CurrencyEnum;
import com.apriori.cid.ui.utils.DecimalPlaceEnum;
import com.apriori.cid.ui.utils.LengthEnum;
import com.apriori.cid.ui.utils.MassEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.TimeEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SettingsTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private DisplayPreferencesPage displayPreferencesPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultsPage productionDefaultPage;
    private AssemblyDefaultsPage assemblyDefaultsPage;
    private UserCredentials currentUser;
    private SelectionPage selectionPage;
    private ComponentInfoBuilder component;
    private AdvancedPage advancedPage;
    private ImportCadFilePage importCadFilePage;
    private UpdateCadFilePage updateCadFilePage;
    private MaterialSelectorPage materialSelectorPage;
    private StockPage stockPage;
    private ComparePage comparePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @AfterEach
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {6283, 6637, 6275, 8883, 6281, 5442, 6282, 6285, 6286, 5429, 6287, 6288, 6284})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS_PLATING.getMaterialName())
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
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("ABS, Plating");
        softAssertions.assertThat(productionDefaultPage.getAnnualVolume()).isEqualTo("3000");
        softAssertions.assertThat(productionDefaultPage.getProductionLife()).isEqualTo("7");
        softAssertions.assertThat(productionDefaultPage.getBatchSize()).isEqualTo("50");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6280})
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
    @TestRail(id = {6279, 6692, 6696})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005_SPONGE.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .logout()
            .login(currentUser)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.POWDER_METAL.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("F-0005 Sponge");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6289, 3605, 6305, 6306, 6307})
    @Description("Manual Batch Quantity cannot be zero, Junk or Decimal")
    public void batchSizeNegativeTest() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0")
            .submit();

        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be greater than 0.");

        productionDefaultPage.inputBatchSize("JUNK");

        softAssertions.assertThat(productionDefaultPage.getBatchSize()).isEmpty();

        productionDefaultPage.inputBatchSize("0.12.00");

        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.inputAnnualVolume("0.12.01");
        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.inputYears("0.12.02");
        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.cancel(ExplorePage.class)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("this is txt");

        softAssertions.assertThat(productionDefaultPage.getBatchSize()).isEqualTo("");

        productionDefaultPage.inputAnnualVolume("this is txt");
        softAssertions.assertThat(productionDefaultPage.getAnnualVolume()).isEqualTo("");

        productionDefaultPage.inputYears("this is txt");
        softAssertions.assertThat(productionDefaultPage.getYears()).isEqualTo("");

        softAssertions.assertAll();
    }

    @Test
    @Issue("CID-1182")
    @TestRail(id = {6308})
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
    @TestRail(id = {6300, 6304})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialSelectorPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .openMaterialSelectorTable();

        assertThat(materialSelectorPage.getListOfMaterials(), hasItems("F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }

    @Test
    @TestRail(id = {6277, 6291})
    @Description("Successfully change the Currency")
    public void changeCurrency() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectCurrency(CurrencyEnum.EUR)
            .selectMass(MassEnum.GRAM)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResultsString("Fully Burdened Cost")).as("Verify Currency Symbol").contains("€");
        softAssertions.assertThat(evaluatePage.getFinishMass()).as("Verify Mass Unit").containsPattern("\\d\\.\\d*g");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6363, 5298})
    @Description("Validate User Preferences are for single user only")
    public void settingsDifferentUsers() {

        UserCredentials user1 = UserUtil.getUser();
        UserCredentials user2 = UserUtil.getUser();
        new UserPreferencesUtil().resetSettings(user1);
        new UserPreferencesUtil().resetSettings(user2);

        loginPage = new CidAppLoginPage(driver);

        displayPreferencesPage = loginPage.login(user1)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.MILLIMETER)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MILLISECOND)
            .selectDecimalPlaces(DecimalPlaceEnum.TWO)
            .submit(ExplorePage.class)
            .logout()
            .login(user2)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.METER)
            .selectMass(MassEnum.KILOGRAM)
            .selectTime(TimeEnum.MINUTE)
            .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
            .submit(ExplorePage.class)
            .logout()
            .login(user1)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.getLength()).isEqualTo("Millimeter");
        softAssertions.assertThat(displayPreferencesPage.getMass()).isEqualTo("Gram");
        softAssertions.assertThat(displayPreferencesPage.getTime()).isEqualTo("Millisecond");
        softAssertions.assertThat(displayPreferencesPage.getDecimalPlaces()).isEqualTo("2");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6368})
    @Description("Validate when a user changes their unit settings comparison values update")
    public void customUnitsDisplayedInComparison() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("M3CapScrew", PLASTIC_MOLDING);
        ComponentInfoBuilder component2 = new ComponentRequestUtil().getComponentWithProcessGroup("Push Pin", PLASTIC_MOLDING);
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MINUTE)
            .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getOutput(component.getComponentName(), component.getScenarioName(), ComparisonCardEnum.MATERIAL_FINISH_MASS)).isEqualTo("0.20809g");
        softAssertions.assertThat(comparePage.getOutput(component.getComponentName(), component.getScenarioName(), ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME)).isEqualTo("0.60952min");
        softAssertions.assertThat(comparePage.getOutput(component.getComponentName(), component.getScenarioName(), ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT)).isEqualTo("$12,347.03378");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6360, 6361, 6367})
    @Description("Validate when a user uploads and costs a component their customer choice is shown")
    public void customUnitsAreDisplayedCorrectly() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getMaterialResultUnit("Total Cycle Time"))
            .as("Verify Default Cycle Time Unit").isEqualToIgnoringCase("s");
        softAssertions.assertThat(evaluatePage.getMaterialResultUnit("Finish Mass"))
            .as("Verify Default Finish Mass Unit").isEqualToIgnoringCase("kg");

        stockPage = evaluatePage.openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Height")).as("Verify Default Stock Height Unit").contains("mm");

        evaluatePage = stockPage.closePanel()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.METER)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MINUTE)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getMaterialResultUnit("Total Cycle Time"))
            .as("Verify Custom Cycle Time Unit").isEqualToIgnoringCase("min");
        softAssertions.assertThat(evaluatePage.getMaterialResultUnit("Finish Mass"))
            .as("Verify Custom Finish Mass Unit").isEqualToIgnoringCase("g");

        stockPage = evaluatePage.openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Height")).as("Verify Custom Stock Height Unit").containsPattern("\\d\\.\\d*m");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8762})
    @Description("Verify default Display Preferences")
    public void defaultDisplayPreferences() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        displayPreferencesPage = loginPage.login(currentUser)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.getUnits()).isEqualTo("MMKS");
        softAssertions.assertThat(displayPreferencesPage.isSystemChecked("Metric")).isTrue();
        softAssertions.assertThat(displayPreferencesPage.getLength()).isEqualTo("Millimeter");
        softAssertions.assertThat(displayPreferencesPage.getMass()).isEqualTo("Kilogram");
        softAssertions.assertThat(displayPreferencesPage.getTime()).isEqualTo("Second");
        softAssertions.assertThat(displayPreferencesPage.getDecimalPlaces()).isEqualTo("2");
        softAssertions.assertThat(displayPreferencesPage.getLanguage()).isEqualTo("English");
        softAssertions.assertThat(displayPreferencesPage.getCurrency()).isEqualTo("USD (United States Dollar)");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11885})
    @Description("Validate that default scenario name can be adjusted through user preferences")
    public void changeTheDefaultScenarioName() {
        currentUser = UserUtil.getUser();
        String testScenarioName = "My Test Scenario Name";

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputScenarioName(testScenarioName)
            .submit(ExplorePage.class)
            .importCadFile();

        assertThat(importCadFilePage.getDefaultScenarioName(), is(testScenarioName));
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {17154, 17155, 17156, 17157, 21547, 21548})
    @Description("Assembly Strategy stuff")
    public void testAssemblyStrategyDropdown() {
        String preferPublic = "Prefer Public Scenarios";
        String preferPrivate = "Prefer Private Scenarios";
        String preferMaturityAndStatus = "Prefer High Maturity and Complete Status";

        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly();

        loginPage = new CidAppLoginPage(driver);
        assemblyDefaultsPage = loginPage.login(assembly.getUser())
            .uploadComponentAndOpen(assembly)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.isAssemblyStrategyDropdownVisible()).as("Verify Dropdown is visible").isTrue();

        assemblyDefaultsPage.selectAssemblyStrategy(preferPublic);

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify Public is selected").isEqualTo(preferPublic);

        importCadFilePage = assemblyDefaultsPage.submit(EvaluatePage.class).importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Public Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage = importCadFilePage.closeDialog(EvaluatePage.class).clickActions().updateCadFile(assembly.getResourceFile());

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Public Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify strategy was saved").isEqualTo(preferPublic);

        assemblyDefaultsPage.selectAssemblyStrategy(preferMaturityAndStatus)
            .submit(EvaluatePage.class)
            .importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer High Maturity and Complete Status. " +
            "This setting can be changed in User Preferences.");

        importCadFilePage.closeDialog(EvaluatePage.class)
            .clickActions()
            .updateCadFile(assembly.getResourceFile());

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer High Maturity and Complete Status. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferMaturityAndStatus);

        assemblyDefaultsPage.selectAssemblyStrategy(preferPrivate)
            .submit(EvaluatePage.class)
            .importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Private Scenarios. " +
            "This setting can be changed in User Preferences.");

        importCadFilePage.closeDialog(EvaluatePage.class)
            .clickActions()
            .updateCadFile(assembly.getResourceFile());

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Private Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was saved").isEqualTo(preferPrivate);

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {17154, 17155, 17156, 17157, 21653, 21654, 21655, 21656})
    @Description("Verify Assembly Strategy Dropdown and Description Cards")
    public void testAssemblyStrategyDropdownCards() {
        currentUser = UserUtil.getUser();

        String preferPublic = "Prefer Public Scenarios";
        String preferPrivate = "Prefer Private Scenarios";
        String preferMaturity = "Prefer High Maturity and Complete Status";
        String sameNameCriteria = "Same name as assembly";
        String mostRecentCriteria = "Most recent";
        String completeCriteria = "Complete status, Highest maturity";
        String maturityCriteria = "Highest maturity";

        String defaultMessage = "No Assembly Association Strategy has been selected. The default strategy, Prefer Private Scenarios, will be used.";

        List<String[]> cardDetails;

        loginPage = new CidAppLoginPage(driver);
        AssemblyDefaultsPage assemblyDefaultsPage = loginPage.login(currentUser)
            .openSettings()
            .goToAssemblyDefaultsTab();

        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.isAssemblyStrategyDropdownVisible()).as("Verify Dropdown is visible").isTrue();

        softAssertions.assertThat(assemblyDefaultsPage.usingDefaultMessage.isDisplayed()).as("Using default message displayed").isTrue();
        softAssertions.assertThat(assemblyDefaultsPage.usingDefaultMessage.getText()).as("Default message presented").isEqualTo(defaultMessage);
        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed for default").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Default selection is Prefer Private").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Default Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Default selection is Prefer Private").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Default Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Default selection is Prefer Private").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Default Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Default selection is Prefer Private").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Default Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage.selectAssemblyStrategy(preferPublic);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify Public is selected").isEqualTo(preferPublic);
        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage = assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify strategy was saved").isEqualTo(preferPublic);

        assemblyDefaultsPage = assemblyDefaultsPage.selectAssemblyStrategy(preferPrivate);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Private Workspace checked initially").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Private Workspace checked initially").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Public Workspace checked second").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Public Workspace checked second").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferPrivate);

        assemblyDefaultsPage = assemblyDefaultsPage.selectAssemblyStrategy(preferMaturity);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 6 cards displayed").isEqualTo(6);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(completeCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(maturityCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(completeCriteria);

        softAssertions.assertThat(cardDetails.get(4)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(4)[3]).as("Verify Criteria").isEqualTo(maturityCriteria);

        softAssertions.assertThat(cardDetails.get(5)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(5)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferMaturity);

        softAssertions.assertAll();
    }
}
