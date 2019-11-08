package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.constants.Constants;

import org.junit.experimental.categories.Categories;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class TestSuiteRunner extends Categories {

    public TestSuiteRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);

        String systemProjectRunId = System.getProperty(Constants.defaultProjectIDKey);
        if (systemProjectRunId != null) {
            Constants.RUN_ID = systemProjectRunId;
            return;
        }

        ProjectRunID projectRunID =  klass.getAnnotation(ProjectRunID.class);
        if(projectRunID != null) {
            Constants.RUN_ID = projectRunID.value();
        }

    }
}
