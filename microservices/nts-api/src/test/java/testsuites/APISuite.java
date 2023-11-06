package testsuites;

import com.apriori.nts.api.tests.EmailsTests;
import com.apriori.nts.api.tests.NotificationsTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    EmailsTests.class,
    NotificationsTests.class
})
public class APISuite {
}
