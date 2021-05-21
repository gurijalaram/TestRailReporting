package testsuite;

import com.apriori.tests.ComponentImageTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ComponentImageTests.class
})
public class CidApiSuite {

}
