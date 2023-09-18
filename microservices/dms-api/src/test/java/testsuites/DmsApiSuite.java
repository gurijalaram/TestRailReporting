package testsuites;

import com.apriori.DmsCommentViewTest;
import com.apriori.DmsCommentsTest;
import com.apriori.DmsDiscussionParticipantTest;
import com.apriori.DmsDiscussionProjectItemTest;
import com.apriori.DmsDiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    DmsDiscussionTest.class,
    DmsCommentsTest.class,
    DmsCommentViewTest.class,
    DmsDiscussionParticipantTest.class,
    DmsDiscussionProjectItemTest.class
})
public class DmsApiSuite {
}
