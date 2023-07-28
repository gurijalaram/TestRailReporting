package testsuites;

import com.apriori.CommentsTest;
import com.apriori.DiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    DiscussionTest.class,
    CommentsTest.class
})
public class DdsApiSuite {
}
