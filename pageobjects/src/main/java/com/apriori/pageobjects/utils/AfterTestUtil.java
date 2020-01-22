package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.DisplayPreferencesEntity;
import com.apriori.apibase.http.builder.common.response.common.ProductionDefaultEntity;
import com.apriori.apibase.http.builder.common.response.common.ToleranceValuesEntity;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;

import io.qameta.allure.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mparker
 */
public class AfterTestUtil {

    private static final Logger logger = LoggerFactory.getLogger(AfterTestUtil.class);
    private APIAuthentication apiAuthentication = new APIAuthentication();

    /**
     * Resets all settings
     *
     * @param userName - userName of logged user
     */
    public void resetAllSettings(String userName) {
        logger.info("Will reset settings for user {}",userName);
        resetDisplayUnits(userName);
        resetColour(userName);
        resetScenarioName(userName);
        resetProductionDefaults(userName);
        resetToleranceSettings(userName);
    }

    /**
     * Resets only display preferences
     *
     * @param userName - userName of logged user
     */
    public void resetDisplayPreferencesUnits(String userName) {
        resetDisplayUnits(userName);
    }

    /**
     * Resets the Tolerance settings back to default
     *
     * @param userName - userName of logged user
     */
    public void resetToleranceSettings(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
            .setAutoLogin(false)
            .setBody(new ToleranceValuesEntity().setToleranceMode("CAD")
                .setUseCadToleranceThreshhold(false))
            .commitChanges()
            .connect()
            .post();

        resetToleranceValues(userName);
    }

    /**
     * Resets the production default settings back to default
     *
     * @param userName - userName of logged user
     */
    @Issue("AP-57904")
    private void resetDisplayUnits(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
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
     *
     * @param userName - userName of logged user
     */
    @Issue("AP-57909")
    private void resetColour(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/preferences/preference?key=selectionColor")
            .setAutoLogin(false)
            .setCustomBody(ColourEnum.YELLOW.getColour())
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the scenario name back to default
     *
     * @param userName - userName of logged user
     */
    @Issue("AP-57908")
    private void resetScenarioName(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/preferences/preference?key=defaultScenarioName")
            .setAutoLogin(false)
            .setCustomBody("Initial")
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the production default settings back to default
     *
     * @param userName - userName of logged user
     */
    @Issue("AP-57908")
    private void resetProductionDefaults(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
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
     *
     * @param userName - userName of logged user
     */
    private void resetToleranceValues(String userName) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
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
}