package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.common.response.common.SelectionEntity;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.enums.UnitsEnum;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;

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
        driver.navigate().to(Constants.cidURL);
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(NO_DEFAULT)
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the display preference settings back to default
     */
    public void resetDisplayPreferences() {
        driver.navigate().to(Constants.cidURL);
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
        driver.navigate().to(Constants.cidURL);
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
        driver.navigate().to(Constants.cidURL);
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openTolerancesTab()
            .selectUseCADModel()
            .uncheckReplaceLessValuesButton();
        new AfterTestUtil(driver).resetSpecificTolValues();
    }

    /**
     * Resets the production default settings back to default
     */
    public void resetAllSettings() {
        driver.navigate().to(Constants.cidURL);
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openSelectionTab()
            .setColour(ColourEnum.YELLOW.getColour());
        new SettingsPage(driver).openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(NO_DEFAULT)
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).openTolerancesTab()
            .selectUseCADModel()
            .uncheckReplaceLessValuesButton();
        new AfterTestUtil(driver).resetSpecificTolValues();
    }

    /**
     * Clears any values in Use specific default values Tolerance PMI Policy
     */
    private void resetSpecificTolValues() {
        new ToleranceSettingsPage(driver).editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "")
            .setTolerance(ToleranceEnum.ROUGHNESSRZ.getToleranceName(), "")
            .setTolerance(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.TRUEPOSITION.getToleranceName(), "")
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CONCENTRICITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PERPENDICULARITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "")
            .setTolerance(ToleranceEnum.TOTALRUNOUT.getToleranceName(), "")
            .setTolerance(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "")
            .setTolerance(ToleranceEnum.SYMMETRY.getToleranceName(), "")
            .save(ToleranceSettingsPage.class);
        new ToleranceSettingsPage(driver).selectAssumeTolerance();

        new SettingsPage(driver).save(ExplorePage.class);
    }

    public void quickAPI() {

//       new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setHeaders(initAuthorizationHeader())
//            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
//            .setAutoLogin(false)
//            .setReturnType(ToleranceValuesEntity.class)
//            .commitChanges()
//            .connect()
//            .get();
//
//        new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setHeaders(initAuthorizationHeader())
//            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
//            .setAutoLogin(false)
//            .setBody(null)
//            .commitChanges()
//            .connect()
//            .post();

//        new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setHeaders(initAuthorizationHeader())
//            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/display-units")
//            .setAutoLogin(false)
//            .setReturnType(DisplayPreferencesEntity.class)
//            .commitChanges()
//            .connect()
//            .get();

//        new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setHeaders(initAuthorizationHeader())
//            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/display-units")
//            .setAutoLogin(false)
//            .setBody(new DisplayPreferencesEntity().setSystemUnits(false))
//            .commitChanges()
//            .connect()
//            .post();

        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/preferences/preference?key=selectionColor")
            .setAutoLogin(false)
            .setBody(new SelectionEntity().setColour("#669900"))
            //.setReturnType(SelectionEntity.class)
            .commitChanges()
            .connect()
            .post();
    }

    private HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
            put("Authorization", "Bearer " + authenticateUser("cfrith@apriori.com", "cfrith"));
            put("apriori.tenantgroup", "default");
            put("apriori.tenant", "default");
            put("Content-Type", "application/vnd.apriori.v1+json");
        }};
    }

    private String authenticateUser(String username, String password) {
        return ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(username, password)
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post()).getAccessToken();
    }
}