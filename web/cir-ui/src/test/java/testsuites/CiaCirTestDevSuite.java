package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        MachiningDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
