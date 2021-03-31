package testsuite;

import com.apriori.tests.controllers.ComponentsControllerTests;
import com.apriori.tests.controllers.IterationsControllerTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ComponentsControllerTests.class,
    IterationsControllerTests.class
})
public class ControllersAPISuite {

}
