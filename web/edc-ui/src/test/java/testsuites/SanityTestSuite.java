package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SanityTests;

@ProjectRunID("809")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SanityTests.class)
@Suite.SuiteClasses({
})
public class SanityTestSuite {
}
