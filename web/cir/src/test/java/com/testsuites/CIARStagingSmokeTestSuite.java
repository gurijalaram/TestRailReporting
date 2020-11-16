package com.testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.testsuites.suiteinterface.CIARStagingSmokeTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CIARStagingSmokeTest.class)
@Suite.SuiteClasses({
    //AdminNavigationTests.class,
    ReportsNavigationTests.class,
    LoginTests.class,
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcComparisonReportTests.class
})

public class CIARStagingSmokeTestSuite {
}
