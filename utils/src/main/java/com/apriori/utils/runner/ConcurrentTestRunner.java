package com.apriori.utils.runner;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */
public class ConcurrentTestRunner extends BlockJUnit4ClassRunner {

    private static final Logger logger_CONCURRENT_TEST_RUNNER = LoggerFactory.getLogger(ConcurrentTestRunner.class);

    public ConcurrentTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        logger_CONCURRENT_TEST_RUNNER.debug("ConcurrentTestRunner constructor");
        String threads = "1";
        if (StringUtils.isNotEmpty(System.getProperty("threadCount"))) {
            threads = System.getProperty("threadCount");
        }
        setScheduler(new ParallelScheduler(threads));

        SuiteRunnerHelper.initProjectRunId(klass);
    }
}
