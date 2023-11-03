package testsuites;

import com.apriori.dds.api.tests.CommentsTest;
import com.apriori.dds.api.tests.DiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DiscussionTest.class,
    CommentsTest.class
})
public class DdsApiSuite {
}
