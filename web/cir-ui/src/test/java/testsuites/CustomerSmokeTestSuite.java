package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CustomerSmokeTests;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    LoginTests.class,
})
public class CustomerSmokeTestSuite {
}