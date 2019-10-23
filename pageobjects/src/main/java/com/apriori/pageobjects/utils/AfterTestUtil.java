package com.apriori.pageobjects.utils;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;

import org.openqa.selenium.WebDriver;

/**
 * @author mparker
 */

public class AfterTestUtil {

    private ExplorePage explorePage;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AfterTestUtil(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    private final String NO_DEFAULT = "<No default specified>";

    /**
     * Resets the production default settings back to default
     */
    public void resetProductionDefaults() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(ProcessGroupEnum.NO_DEFAULT.getProcessGroup())
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .selectMaterial(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the display preference settings back to default
     */
    public void resetDisplayPreferences() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .changeCurrency(CurrencyEnum.USD.getCurrency());
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the selection colour back to default
     */
    public void resetSelectionColour() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openSelectionTab()
            .setColour(ColourEnum.YELLOW.getColour());
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the Tolerance settings back to default
     */
    public void resetToleranceSettings() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the production default settings back to default
     */
    public void resetAllSettings() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openSelectionTab()
            .setColour(ColourEnum.YELLOW.getColour());
        new SettingsPage(driver).openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(ProcessGroupEnum.NO_DEFAULT.getProcessGroup())
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .selectMaterial(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).openTolerancesTab()
            .selectAssumeTolerance();
        new SettingsPage(driver).save(ExplorePage.class);
    }
}