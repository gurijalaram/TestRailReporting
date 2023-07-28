package testsuites;

import com.apriori.DmsCommentViewTest;
import com.apriori.DmsCommentsTest;
import com.apriori.DmsDiscussionParticipantTest;
import com.apriori.DmsDiscussionProjectItemTest;
import com.apriori.DmsDiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    DmsDiscussionTest.class,
    DmsCommentsTest.class,
    DmsCommentViewTest.class,
    DmsDiscussionParticipantTest.class,
    DmsDiscussionProjectItemTest.class
})
public class DmsApiSuite {
}
