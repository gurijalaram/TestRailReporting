package testsuites;

import com.apriori.ach.api.tests.AchApplicationDataTests;
import com.apriori.ach.api.tests.AchCurrentUserTests;
import com.apriori.ach.api.tests.AchCustomerUsersTests;
import com.apriori.ach.api.tests.AchCustomersTests;
import com.apriori.ach.api.tests.AchDeploymentsTest;
import com.apriori.ach.api.tests.AchEnablementsSupportTests;
import com.apriori.ach.api.tests.AchNotificationsTests;
import com.apriori.ach.api.tests.AchPeopleTests;
import com.apriori.ach.api.tests.AchUserPreferencesTests;
import com.apriori.ach.api.tests.AchUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    AchApplicationDataTests.class,
    AchCurrentUserTests.class,
    AchCustomersTests.class,
    AchCustomerUsersTests.class,
    AchDeploymentsTest.class,
    AchEnablementsSupportTests.class,
    AchNotificationsTests.class,
    AchPeopleTests.class,
    AchUserPreferencesTests.class,
    AchUsersTests.class
})
public class AchAPISuite {
}