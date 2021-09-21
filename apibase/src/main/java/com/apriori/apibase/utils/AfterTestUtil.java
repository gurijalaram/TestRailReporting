package com.apriori.apibase.utils;

import com.apriori.apibase.enums.BaseAPIEnum;
import com.apriori.apibase.services.response.objects.DisplayPreferencesEntity;
import com.apriori.apibase.services.response.objects.ProductionDefaultEntity;
import com.apriori.apibase.services.response.objects.ToleranceValuesEntity;
import com.apriori.apibase.services.response.objects.UnitSystemSettingEntity;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.LengthEnum;
import com.apriori.utils.enums.MassEnum;
import com.apriori.utils.enums.TimeEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mparker
 */
// TODO ALL: test this functionality
public class AfterTestUtil {

    private static final Logger logger = LoggerFactory.getLogger(AfterTestUtil.class);
    private APIAuthentication apiAuthentication = new APIAuthentication();

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
     * Resets the Tolerance settings back to default
     *
     * @param username - username of logged user
     */
    public void resetToleranceSettings(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_TOLERANCE_POLICY, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .body(new ToleranceValuesEntity().setToleranceMode("CAD")
                .setUseCadToleranceThreshhold(false));

        HTTP2Request.build(requestEntity).post();

        resetToleranceValues(username);
    }

    /**
     * Resets the production default settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57904")
    private void resetDisplayUnits(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_DISPLAY_UNITS, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .body(new DisplayPreferencesEntity().setUnitSystemSetting(new UnitSystemSettingEntity().setType("simple")
                .setName(UnitsEnum.CUSTOM.getUnits())
                .setMetric(true)
                .setLength(LengthEnum.MILLIMETER.getLength())
                .setMass(MassEnum.KILOGRAM.getMass())
                .setTime(TimeEnum.SECOND.getTime())
                .setDecimalPlaces(2)
                .setSystem(true)
                .setCustom(false))
            );

        HTTP2Request.build(requestEntity).post();
    }

    /**
     * Resets the decimal places to default
     *
     * @param username - username of logged user
     */
    private void resetDecimalPlaces(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_PREFERENCES, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .customBody("2");

        HTTP2Request.build(requestEntity).post();
    }

    /**
     * Resets the Tolerance settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57909")
    private void resetColour(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_PREFERENCES_WITH_COLOR, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .customBody(ColourEnum.YELLOW.getColour());

        HTTP2Request.build(requestEntity).post();
    }

    /**
     * Resets the scenario name back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57908")
    private void resetScenarioName(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_PREFERENCES_SCENARIO_NAME, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .customBody("Initial");

        HTTP2Request.build(requestEntity).post();
    }

    /**
     * Resets the production default settings back to default
     *
     * @param username - username of logged user
     */
    @Issue("AP-57908")
    private void resetProductionDefaults(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_PRODUCTION_DEFAULTS, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .body(new ProductionDefaultEntity().setPg(null)
                .setVpe(null)
                .setMaterialCatalogName(null)
                .setAnnualVolume(null)
                .setProductionLife(null)
                .setBatchSizeMode(false)
            );

        HTTP2Request.build(requestEntity).post();
    }

    /**
     * Resets the production default settings back to default
     *
     * @param username - username of logged user
     */
    private void resetToleranceValues(String username) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_TOLERANCE_DEFAULTS, null)
            .headers(apiAuthentication.initAuthorizationHeader(username))
            .body(new ToleranceValuesEntity().setMinCadToleranceThreshhold(5.55)
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
                .setSymmetryOverride(null));

        HTTP2Request.build(requestEntity).post();
    }
}