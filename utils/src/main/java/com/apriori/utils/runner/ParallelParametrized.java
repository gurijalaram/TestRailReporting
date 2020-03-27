package com.apriori.utils.runner;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.Parameterized;

/**
 * @author kpatel
 */
public class ParallelParametrized extends Parameterized {

    public ParallelParametrized(Class<?> klass) throws Throwable {
        super(klass);
        String threads = "1";
        if (StringUtils.isNotEmpty(System.getProperty("threadCounts"))) {
            threads = System.getProperty("threadCounts");
        }
        setScheduler(new ParallelScheduler(threads));

        SuiteRunnerHelper.initProjectRunId(klass);
    }


}
