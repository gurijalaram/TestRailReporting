package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.ach.api.tests.AchCurrentUserTests;
import com.apriori.acs.api.tests.AllMaterialStocksInfoTests;
import com.apriori.ats.api.tests.AtsAuthorization;
import com.apriori.bcm.api.tests.WorksheetTests;
import com.apriori.bcs.api.tests.BatchPartTest;
import com.apriori.cas.api.tests.CasCustomersTests;
import com.apriori.cas.api.tests.CasUsersTests;
import com.apriori.cds.api.tests.CdsCustomersTests;
import com.apriori.cic.api.tests.CicAgentTest;
import com.apriori.cid.api.tests.evaluate.DataCreationTests;
import com.apriori.cid.ui.tests.evaluate.CostAllCadTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.apriori.cis.api.tests.CisBidPackageProjectsTest;
import com.apriori.cmp.api.tests.CmpComparisonTests;
import com.apriori.css.api.tests.CssSearchTests;
import com.apriori.dds.api.tests.DdsCommentsTest;
import com.apriori.dds.api.tests.DdsDiscussionTest;
import com.apriori.dms.api.tests.DmsCommentsTest;
import com.apriori.fms.api.tests.FileManagementControllerTest;
import com.apriori.gcd.api.tests.GcdTreesTests;
import com.apriori.nts.api.tests.EmailsTests;
import com.apriori.qds.api.tests.BidPackageProjectUserTest;
import com.apriori.qds.api.tests.QdsBidPackageProjectsTest;
import com.apriori.qms.api.tests.QmsBidPackageProjectsTest;
import com.apriori.qms.api.tests.QmsScenarioDiscussionTest;
import com.apriori.sds.api.tests.ComponentsTest;
import com.apriori.vds.api.tests.DigitalFactoriesTest;
import com.apriori.vds.api.tests.ProcessGroupsTest;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AtsAuthorization.class,
    AchCurrentUserTests.class,
    AllMaterialStocksInfoTests.class,
    AtsAuthorization.class,
    BatchPartTest.class,
    BidPackageProjectUserTest.class,
    QdsBidPackageProjectsTest.class,
    QmsBidPackageProjectsTest.class,
    CasCustomersTests.class,
    CasUsersTests.class,
    CdsCustomersTests.class,
    CicAgentTest.class,
    CisBidPackageProjectsTest.class,
    CostAllCadTests.class,
    DataCreationTests.class,
    DdsCommentsTest.class,
    DdsDiscussionTest.class,
    DmsCommentsTest.class,
    CmpComparisonTests.class,
    CssSearchTests.class,
    WorksheetTests.class,
    FileManagementControllerTest.class,
    GcdTreesTests.class,
    EmailsTests.class,
    ComponentsTest.class,
    DigitalFactoriesTest.class,
    ProcessGroupsTest.class,
    QmsScenarioDiscussionTest.class,
    SheetMetalDtcReportTests.class
    })
@IncludeTags(API_SANITY)
public class ApiSanityTestSuite {
}
