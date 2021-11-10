package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.NewCustomerTests;

import com.login.LoginTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    NewCustomerTests.class,
    LoginTests.class
})
public final class RegressionTestSuite {
}
