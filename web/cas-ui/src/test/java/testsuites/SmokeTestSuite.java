package testsuites;

import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.NewCustomerTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory({SmokeTest.class})
@Suite.SuiteClasses({
    NewCustomerTests.class
})
public final class SmokeTestSuite {
}
