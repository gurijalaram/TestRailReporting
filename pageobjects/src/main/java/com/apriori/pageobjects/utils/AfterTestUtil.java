package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.common.response.common.DisplayPreferencesEntity;
import com.apriori.apibase.http.builder.common.response.common.ProductionDefaultEntity;
import com.apriori.apibase.http.builder.common.response.common.ToleranceValuesEntity;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Issue;

import java.util.HashMap;

/**
 * @author mparker
 */

public class AfterTestUtil {

    /**
     * Resets all settings
     */
    public void resetAllSettings() {
        resetDisplayUnits();
        resetColour();
        resetScenarioName();
        resetProductionDefaults();
        resetToleranceSettings();
    }

    /**
     * Resets only display preferences
     */
    public void resetDisplayPreferencesUnits() {
        resetDisplayUnits();
    }

    /**
     * Resets the Tolerance settings back to default
     */
    public void resetToleranceSettings() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
            .setAutoLogin(false)
            .setBody(new ToleranceValuesEntity().setToleranceMode("CAD")
                .setUseCadToleranceThreshhold(false))
            .commitChanges()
            .connect()
            .post();

        resetToleranceValues();
    }

    /**
     * Resets the production default settings back to default
     */
    @Issue("AP-57904")
    private void resetDisplayUnits() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/display-units")
            .setAutoLogin(false)
            .setBody(new DisplayPreferencesEntity().setSystemUnits(true)
                .setCurrencyCode(CurrencyEnum.USD.getCurrency()))
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the Tolerance settings back to default
     */
    @Issue("AP-57909")
    private void resetColour() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/preferences/preference?key=selectionColor")
            .setAutoLogin(false)
            .setCustomBody(ColourEnum.YELLOW.getColour())
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the scenario name back to default
     */
    @Issue("AP-57908")
    private void resetScenarioName() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/preferences/preference?key=defaultScenarioName")
            .setAutoLogin(false)
            .setCustomBody("Initial")
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the production default settings back to default
     */
    @Issue("AP-57908")
    private void resetProductionDefaults() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/production-defaults")
            .setBody(new ProductionDefaultEntity().setPg(null)
                .setVpe(null)
                .setMaterialCatalogName(null)
                .setAnnualVolume(null)
                .setProductionLife(null)
                .setBatchSizeMode(false))
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the production default settings back to default
     */
    private void resetToleranceValues() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
            .setAutoLogin(false)
            .setBody(new ToleranceValuesEntity().setMinCadToleranceThreshhold(5.55)
                .setCadToleranceReplacement(5.55)
                .setToleranceOverride(null)
                .setRoughnessOverride(null)
                .setRoughnessRzOverride(null)
                .setDiamToleranceOverride(null)
                .setPositionToleranceOverride(null)
                .setBendAngleToleranceOverride(null)
                .setCircularityOverride(null)
                .setConcentricityOverride(null)
                .setCylindricityOverride(null)
                .setFlatnessOverride(null)
                .setParallelismOverride(null)
                .setPerpendicularityOverride(null)
                .setProfileOfSurfaceOverride(null)
                .setRunoutOverride(null)
                .setTotalRunoutOverride(null)
                .setStraightnessOverride(null)
                .setSymmetryOverride(null))
            .commitChanges()
            .connect()
            .post();
    }

    @SuppressWarnings("CheckStyle")
    private HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
            put("Authorization", "Bearer " + authenticateUser(UserUtil.getUser().getUsername(), UserUtil.getUser().getUsername().split("@")[0]));
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