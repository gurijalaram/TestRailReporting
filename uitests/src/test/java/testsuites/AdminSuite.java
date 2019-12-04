package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;
import ciadmintests.navigation.NavigationTests;
import com.apriori.utils.ProjectRunID;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
        NavigationTests.class
})
public class AdminSuite {
}