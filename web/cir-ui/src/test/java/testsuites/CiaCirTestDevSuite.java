package testsuites;

import com.adhocview.AdHocViewTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AdHocViewTests.class
})

public class CiaCirTestDevSuite {
}
