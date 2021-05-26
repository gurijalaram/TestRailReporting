package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class
})

public class CiaCirTestDevSuite {
}
