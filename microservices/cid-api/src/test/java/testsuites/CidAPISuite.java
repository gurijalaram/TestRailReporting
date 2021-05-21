package testsuites;

import com.apriori.CidWorkorderAPITests;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.categories.CidAPITest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CidAPITest.class)
@Suite.SuiteClasses({
        CidWorkorderAPITests.class
})

public class CidAPISuite {
}
