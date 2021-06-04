package tests.suite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.AtsAuthorization;

@ProjectRunID("375")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        AtsAuthorization.class
})
public class AtsAPISuite {

}
