package com.apriori.vds.tests.suite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.AccessControlsTest;
import com.apriori.vds.tests.ConfigurationTest;
import com.apriori.vds.tests.CustomAttributesTest;
import com.apriori.vds.tests.CustomizationsTest;
import com.apriori.vds.tests.DigitalFactoriesTest;

import com.apriori.vds.tests.ProcessGroupMaterialsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("704")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AccessControlsTest.class,
    ConfigurationTest.class,
    CustomAttributesTest.class,
    CustomizationsTest.class,
    DigitalFactoriesTest.class,
    ProcessGroupMaterialsTest.class
})
public class APISuite {
}
