package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;


public class Constants {

    private static final ConstantsInit constantsInit = ConfigFactory.create(ConstantsInit.class);

    public static final Level consoleLogLevel = Level.parse(constantsInit.consoleLogLevelData());
    public static final String url = constantsInit.url();
    public static final String schemaBasePath = constantsInit.schemaBasePath();
    public static final String internalApiURL = constantsInit.internalApiURL();
    public static final String GRID_SERVER_URL = constantsInit.gridServerUrl();
    public static final String cidURL = constantsInit.cidURL();
    public static final String cirURL = constantsInit.cirURL();
}