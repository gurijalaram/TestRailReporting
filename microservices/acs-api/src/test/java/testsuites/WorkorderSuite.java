package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.WorkorderTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(WorkorderTest.class)
@Suite.SuiteClasses({
    WorkorderAPITests.class
})

public class WorkorderSuite {
}
