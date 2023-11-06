package testsuites;

import com.apriori.cia.ui.tests.navigation.AdminNavigationTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(AdminNavigationTests.class)
public class AdminSuite {
}