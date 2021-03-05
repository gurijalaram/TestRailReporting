package com.apriori.utils.runner;

import org.apache.commons.lang3.StringUtils;
import org.junit.experimental.categories.Categories;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */
public class CategorySuiteRunner extends Categories {

    private static final Logger logger_CONCURRENT_SUITE_RUNNER = LoggerFactory.getLogger(CategorySuiteRunner.class);

    public CategorySuiteRunner(final Class<?> klass) throws InitializationError {
        super(klass, new AllDefaultPossibilitiesBuilder(true));
        logger_CONCURRENT_SUITE_RUNNER.debug("ConcurrentTestRunner constructor");
        String threads = "1";
        if (StringUtils.isNotEmpty(System.getProperty("threadCountClasses"))) {
            threads = System.getProperty("threadCountClasses");
        } else if (StringUtils.isEmpty(System.getProperty("threadCountClasses")) && StringUtils.isNotEmpty(System.getProperty("threadCounts"))) {
            threads = System.getProperty("threadCounts");
        }
        setScheduler(new ParallelScheduler(threads));

        SuiteRunnerHelper.initProjectRunId(klass);
    }
}
