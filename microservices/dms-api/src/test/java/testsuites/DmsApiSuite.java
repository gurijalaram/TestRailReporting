package testsuites;

import com.apriori.dms.api.tests.DmsCommentViewTest;
import com.apriori.dms.api.tests.DmsCommentsTest;
import com.apriori.dms.api.tests.DmsDiscussionParticipantTest;
import com.apriori.dms.api.tests.DmsDiscussionProjectItemTest;
import com.apriori.dms.api.tests.DmsDiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DmsDiscussionTest.class,
    DmsCommentsTest.class,
    DmsCommentViewTest.class,
    DmsDiscussionParticipantTest.class,
    DmsDiscussionProjectItemTest.class
})
public class DmsApiSuite {
}
