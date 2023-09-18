package testsuites;

import com.apriori.ach.tests.AchApplicationDataTests;
import com.apriori.ach.tests.AchCurrentUserTests;
import com.apriori.ach.tests.AchCustomersTests;
import com.apriori.ach.tests.AchEnablementsSupportTests;
import com.apriori.ach.tests.AchNotificationsTests;
import com.apriori.ach.tests.AchPeopleTests;
import com.apriori.ach.tests.AchUserPreferencesTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectPackages("com.apriori")
@SelectClasses({
    AchApplicationDataTests.class,
    AchCurrentUserTests.class,
    AchCustomersTests.class,
    AchEnablementsSupportTests.class,
    AchNotificationsTests.class,
    AchPeopleTests.class,
    AchUserPreferencesTests.class
})
public class AchAPISuite {
}