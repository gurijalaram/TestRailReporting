package testsuite;

import com.apriori.tests.controllers.ComponentsControllerTests;
import com.apriori.tests.controllers.IterationsControllerTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// TODO: 10/09/2021 cn - suite and test to be deleted as the are covered by sds
//@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ComponentsControllerTests.class,
    IterationsControllerTests.class
})
public class ControllersAPISuite {

}
