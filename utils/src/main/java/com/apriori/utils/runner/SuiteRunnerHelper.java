package com.apriori.utils.runner;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.constants.CommonConstants;

public class SuiteRunnerHelper {

    static void initProjectRunId(Class<?> klass) {
        String systemProjectRunId = System.getProperty(CommonConstants.DEFAULT_PROJECT_ID_KEY);
        if (systemProjectRunId != null) {
            CommonConstants.RUN_ID = systemProjectRunId;
            return;
        }

        ProjectRunID projectRunID = klass.getAnnotation(ProjectRunID.class);
        if (projectRunID != null) {
            CommonConstants.RUN_ID = projectRunID.value();
        }
    }
}
