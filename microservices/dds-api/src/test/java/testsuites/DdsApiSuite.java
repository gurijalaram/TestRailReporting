package testsuites;

import com.apriori.CommentsTest;
import com.apriori.DiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    DiscussionTest.class,
    CommentsTest.class
})
public class DdsApiSuite {
}
