package main.java.runner;

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

    private static final Logger logger_ConcurrentSuiteRunner = LoggerFactory.getLogger(CategorySuiteRunner.class);

    public CategorySuiteRunner(final Class<?> klass) throws InitializationError {
        super(klass, new AllDefaultPossibilitiesBuilder(true));
        logger_ConcurrentSuiteRunner.debug("ConcurrentTestRunner constructor");
        String threads = "1";
        if (StringUtils.isNotEmpty(System.getProperty("threadCountClasses"))) {
            threads = System.getProperty("threadCountClasses");
        } else if (StringUtils.isEmpty(System.getProperty("threadCountClasses")) && StringUtils.isNotEmpty(System.getProperty("threadCount"))) {
            threads = System.getProperty("threadCount");
        }
        setScheduler(new ParallelScheduler(threads));
    }
}
