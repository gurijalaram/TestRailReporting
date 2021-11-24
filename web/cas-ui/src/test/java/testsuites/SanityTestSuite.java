package testsuites;

import com.apriori.testsuites.categories.SanityTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory({SanityTest.class})
@Suite.SuiteClasses({
    RegressionTestSuite.class
})
public class SanityTestSuite {
}
