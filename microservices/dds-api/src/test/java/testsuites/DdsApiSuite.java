package testsuites;

import com.apriori.dds.api.tests.DdsCommentsTest;
import com.apriori.dds.api.tests.DdsDiscussionTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DdsDiscussionTest.class,
    DdsCommentsTest.class
})
public class DdsApiSuite {
}
