package testsuites;

import com.apriori.dms.tests.DmsCommentViewTest;
import com.apriori.dms.tests.DmsCommentsTest;
import com.apriori.dms.tests.DmsDiscussionParticipantTest;
import com.apriori.dms.tests.DmsDiscussionTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1714")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    DmsDiscussionTest.class,
    DmsCommentsTest.class,
    DmsCommentViewTest.class,
    DmsDiscussionParticipantTest.class
})
public class DmsApiSuite {
}
