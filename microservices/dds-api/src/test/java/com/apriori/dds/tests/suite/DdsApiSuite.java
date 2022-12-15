package com.apriori.dds.tests.suite;

import com.apriori.dds.tests.CommentsTest;
import com.apriori.dds.tests.DiscussionTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1713")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    DiscussionTest.class,
    CommentsTest.class
})
public class DdsApiSuite {
}
