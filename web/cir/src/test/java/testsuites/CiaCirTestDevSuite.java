package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class,
        CycleTimeValueTrackingTests.class,
        DesignOutlierIdentificationReportTests.class
})

public class CiaCirTestDevSuite {
}
