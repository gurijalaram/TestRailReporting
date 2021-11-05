package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.NewCustomerTests;

import com.login.LoginTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.apriori.testsuites.categories.SmokeTest;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory({SmokeTest.class})
@Suite.SuiteClasses({
    NewCustomerTests.class,
    LoginTests.class
})
public final class SmokeTestSuite {
}
