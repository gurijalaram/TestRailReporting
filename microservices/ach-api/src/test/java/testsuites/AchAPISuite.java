package testsuites;

import com.apriori.ach.tests.AchApplicationDataTests;
import com.apriori.ach.tests.AchCurrentUserTests;
import com.apriori.ach.tests.AchCustomersTests;
import com.apriori.ach.tests.AchNotificationsTests;
import com.apriori.ach.tests.AchPeopleTests;
import com.apriori.ach.tests.AchUserPreferencesTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("2353")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AchApplicationDataTests.class,
    AchCurrentUserTests.class,
    AchCustomersTests.class,
    AchNotificationsTests.class,
    AchPeopleTests.class,
    AchUserPreferencesTests.class
})
public class AchAPISuite {
}