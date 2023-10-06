package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.AtsAuthorization;
import com.apriori.BatchPartTest;
import com.apriori.BidPackageProjectUserTest;
import com.apriori.CasCustomersTests;
import com.apriori.CasUsersTests;
import com.apriori.CdsCustomersTests;
import com.apriori.CicAgentTest;
import com.apriori.CisBidPackageProjectsTest;
import com.apriori.CmpComparisonTests;
import com.apriori.CommentsTest;
import com.apriori.ComponentsTest;
import com.apriori.CssSearchTests;
import com.apriori.DigitalFactoriesTest;
import com.apriori.DiscussionTest;
import com.apriori.DmsCommentsTest;
import com.apriori.EmailsTests;
import com.apriori.FileManagementControllerTest;
import com.apriori.GcdTreesTests;
import com.apriori.LineItemsTest;
import com.apriori.ProcessGroupsTest;
import com.apriori.QdsBidPackageProjectsTest;
import com.apriori.QmsBidPackageProjectsTest;
import com.apriori.QmsScenarioDiscussionTest;
import com.apriori.UserTests;
import com.apriori.ach.tests.AchCurrentUserTests;
import com.apriori.evaluate.CostAllCadTests;
import com.apriori.evaluate.DataCreationTests;
import com.apriori.workorders.WorkorderAPITests;

import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AtsAuthorization.class,
    AchCurrentUserTests.class,
    WorkorderAPITests.class,
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
    CommentsTest.class,
    DataCreationTests.class,
    DiscussionTest.class,
    DmsCommentsTest.class,
    CmpComparisonTests.class,
    CssSearchTests.class,
    UserTests.class,
    LineItemsTest.class,
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
