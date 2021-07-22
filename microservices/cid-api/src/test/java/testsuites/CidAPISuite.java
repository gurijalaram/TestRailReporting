package testsuites;

import com.apriori.WorkorderAPITests;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CidAPITest.class)
@Suite.SuiteClasses({
        WorkorderAPITests.class
})

public class CidAPISuite {
}
