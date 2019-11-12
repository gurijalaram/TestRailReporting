package com.apriori.utils.runner;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.constants.Constants;

public class SuiteRunnerHelper {

    static void initProjectRunId(Class<?> klass) {
        String systemProjectRunId = System.getProperty(Constants.defaultProjectIDKey);
        if (systemProjectRunId != null) {
            Constants.RUN_ID = systemProjectRunId;
            return;
        }

        ProjectRunID projectRunID = klass.getAnnotation(ProjectRunID.class);
        if (projectRunID != null) {
            Constants.RUN_ID = projectRunID.value();
        }
    }
}
