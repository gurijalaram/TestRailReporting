package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.navigation.AdminNavigationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AdminNavigationTests.class
})

public class OnPremCiaSuite {
}
