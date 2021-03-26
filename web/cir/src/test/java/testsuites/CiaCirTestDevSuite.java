package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
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
