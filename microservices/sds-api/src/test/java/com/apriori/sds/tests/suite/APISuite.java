package com.apriori.sds.tests.suite;

import com.apriori.sds.tests.ApFilesTest;
import com.apriori.sds.tests.ComponentsTest;
import com.apriori.sds.tests.ConnectionsTest;
import com.apriori.sds.tests.CostingTemplatesTest;
import com.apriori.sds.tests.IterationsTest;
import com.apriori.sds.tests.ScenarioAssociationsTest;
import com.apriori.sds.tests.ScenarioIterationsTest;
import com.apriori.sds.tests.ScenariosTest;
import com.apriori.sds.tests.SecondaryProcessesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("656")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ApFilesTest.class,
    ComponentsTest.class,
    ConnectionsTest.class,
    CostingTemplatesTest.class,
    IterationsTest.class,
    ScenarioIterationsTest.class,
    ScenariosTest.class
})
public class APISuite {
}

