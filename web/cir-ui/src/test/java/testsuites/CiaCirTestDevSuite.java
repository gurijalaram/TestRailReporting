package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        DesignOutlierIdentificationReportTests.class,
        DesignOutlierIdentificationDetailsReportTests.class
})

public class CiaCirTestDevSuite {
}
