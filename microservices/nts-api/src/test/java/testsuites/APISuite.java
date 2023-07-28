package testsuites;

import com.apriori.EmailsTests;
import com.apriori.NotificationsTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    EmailsTests.class,
    NotificationsTests.class
})
public class APISuite {
}
