package com.apriori.enums;

public enum PreferencesEnum {
    ASSEMBLY_STRATEGY("assembly.associationStrategyPreset"),
    AREA_UNITS("display.areaUnits"),
    CURRENCY("display.currency"),
    UNITS_GROUP("display.unitsGroup"),
    LENGTH_UNITS("display.lengthUnits"),
    MASS_UNITS("display.massUnits"),
    TIME_UNITS("display.timeUnits"),
    DECIMAL_PLACES("display.decimalPlaces"),
    LANGUAGE("display.language"),
    SELECTION_COLOUR("display.selectionColor"),
    DEFAULT_SCENARIO_NAME("production.defaultScenarioName"),
    DEFAULT_PROCESS_GROUP("production.defaultProcessGroup"),
    DEFAULT_DIGITAL_FACTORY("production.defaultDigitalFactory"),
    DEFAULT_MATERIAL_CATALOG_NAME("production.defaultMaterialCatalogName"),
    CAD_TOLERANCE_THRESHOLD("tolerance.useCadToleranceThreshold"),
    DEFAULT_MATERIAL_NAME("production.defaultMaterialName"),
    DEFAULT_ANNUAL_VOLUME("production.defaultAnnualVolume"),
    DEFAULT_PRODUCTION_LIFE("production.defaultProductionLife"),
    DEFAULT_BATCH_SIZE("production.defaultBatchSize"),
    TOLERANCE_MODE("tolerance.toleranceMode");

    private final String preference;

    PreferencesEnum(String preference) {
        this.preference = preference;
    }

    public String getPreference() {
        return this.preference;
    }
}
