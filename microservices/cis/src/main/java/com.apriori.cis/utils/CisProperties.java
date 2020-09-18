package com.apriori.cis.utils;

import org.aeonbits.owner.ConfigFactory;

public class CisProperties {

    private static final CisPropertiesInit cisPropertiesInit = ConfigFactory.create(CisPropertiesInit.class);
    
    private static String costingThreads;
    private static String pollingTimeout;

    public static Integer getCostingThreads() {
        if (costingThreads == null) {
            costingThreads = System.getProperty("costingThreads", cisPropertiesInit.costingThreads());
        }
        return Integer.valueOf(costingThreads);
    }

    public static void setCostingThreads(Integer costingThreads) {
        CisProperties.costingThreads = costingThreads.toString();
    }

    public static Integer getPollingTimeout() {
        if (pollingTimeout == null) {
            pollingTimeout = System.getProperty("pollingTimeout", cisPropertiesInit.pollingTimeout());
        }
        return Integer.valueOf(pollingTimeout);
    }

    public static void setPollingTimeout(Integer pollingTimeout) {
        CisProperties.pollingTimeout = pollingTimeout.toString();
    }

}
