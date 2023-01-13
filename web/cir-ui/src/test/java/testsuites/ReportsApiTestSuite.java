package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CastingDtcReportTests.class
})
public class ReportsApiTestSuite {
}
