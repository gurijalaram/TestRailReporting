package com.apriori.apibase.utils;

import com.apriori.apibase.services.response.objects.DisplayPreferencesEntity;
import com.apriori.apibase.services.response.objects.ProductionDefaultEntity;
import com.apriori.apibase.services.response.objects.ToleranceValuesEntity;
import com.apriori.apibase.services.response.objects.UnitSystemSettingEntity;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.LengthEnum;
import com.apriori.utils.enums.MassEnum;
import com.apriori.utils.enums.TimeEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http.builder.service.HTTPRequest;

import io.qameta.allure.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mparker
 */
public class AfterTestUtil {

    private static final Logger logger = LoggerFactory.getLogger(AfterTestUtil.class);
    APIAuthentication apiAuthentication = new APIAuthentication();
    private String baseUrl = System.getProperty("baseUrl");

    /**
     * Resets all settings
     *
     * @param username - username of logged user
     */
    public void resetAllSettings(String username) {
        logger.info("Will reset settings for user {}", username);
        resetDisplayUnits(username);
        resetColour(username);
        resetScenarioName(username);
        resetProductionDefaults(username);
        resetToleranceSettings(username);
        resetDecimalPlaces(username);
    }

    /**
     * Resets only display preferences
     *
     * @param username - username of logged user
     */
    public void resetDisplayPreferencesUnits(String username) {
        resetDisplayUnits(username);
    }

    /**
     * Resets the Tolerance settings back to default
     *
     * @param username - username of logged user
     */
    public void resetToleranceSettings(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/tolerance-policy-defaults")
            .setAutoLogin(false)
            .setBody(new ToleranceValuesEntity().setToleranceMode("CAD")
                .setUseCadToleranceThreshhold(false))
            .commitChanges()
            .connect()
            .post();

        resetToleranceValues(username);
    }

    /**
     * Resets the production default settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57904")
    private void resetDisplayUnits(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/display-units")
            .setAutoLogin(false)
            .setBody(new DisplayPreferencesEntity().setUnitSystemSetting(new UnitSystemSettingEntity().setType("simple")
                .setName(UnitsEnum.CUSTOM.getUnits())
                .setMetric(true)
                .setLength(LengthEnum.MILLIMETER.getLength())
                .setMass(MassEnum.KILOGRAM.getMass())
                .setTime(TimeEnum.SECOND.getTime())
                .setDecimalPlaces(2)
                .setSystem(true)
                .setCustom(false)))
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the decimal places to default
     *
     * @param username - username of logged user
     */
    private void resetDecimalPlaces(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/preferences/preference?key=cost.table.decimal.places")
            .setAutoLogin(false)
            .setCustomBody("2")
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the Tolerance settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57909")
    private void resetColour(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/preferences/preference?key=selectionColor")
            .setAutoLogin(false)
            .setCustomBody(ColourEnum.YELLOW.getColour())
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the scenario name back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57908")
    private void resetScenarioName(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/preferences/preference?key=defaultScenarioName")
            .setAutoLogin(false)
            .setCustomBody("Initial")
            .commitChanges()
            .connect()
            .post();
    }

    /**
     * Resets the production default settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57908")
    private void resetProductionDefaults(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/production-defaults")
            .setAutoLogin(false)
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
     * @param username - username of logged user
     */
    private void resetToleranceValues(String username) {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(apiAuthentication.initAuthorizationHeader(username))
            .setEndpoint(baseUrl + "ws/workspace/users/me/tolerance-policy-defaults")
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