package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostScenarioTests;
import com.evaluate.PublishTests;
import com.explore.UploadComponentTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SanityTests;

@ProjectRunID("769")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SanityTests.class)
@Suite.SuiteClasses({
    UploadComponentTests.class,
    CostScenarioTests.class,
    PublishTests.class
})
public class SanityTestSuite {
}
