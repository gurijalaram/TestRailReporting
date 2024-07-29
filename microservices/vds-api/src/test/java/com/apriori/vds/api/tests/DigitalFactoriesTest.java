package com.apriori.vds.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.models.response.digital.factories.DigitalFactory;
import com.apriori.vds.api.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class DigitalFactoriesTest {
    private RequestEntityUtil requestEntityUtil;
    private VDSTestUtil vdsTestUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        vdsTestUtil = new VDSTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {8034})
    @Description("POST create or updates a Digital Factory for a customer.")
    @Disabled
    public void postDigitalFactories() {
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {8030})
    @Description("Get a list of Digital Factories for a specific customer.")
    public void getDigitalFactories() {
        vdsTestUtil.getDigitalFactories();
    }

    @Test
    @TestRail(id = {8031})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getDigitalFactoriesByIdentity() {
        DigitalFactory digitalFactories = vdsTestUtil.getDigitalFactoryById();

        assertThat(digitalFactories).isNotNull();
    }

    @Test
    @TestRail(id = {8035})
    @Description("POST create or updates a VPEs for a customer.")
    @Disabled
    public void postVPEs() {
    }

    @Test
    @TestRail(id = {8032})
    @Description("Get a list of Digital Factories for a specific customer.")
    public void getVPEs() {
        List<DigitalFactory> vpes = vdsTestUtil.getVpes();

        assertThat(vpes).isNotEmpty();
    }

    @Test
    @TestRail(id = {8033})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getVPEByIdentity() {
        DigitalFactory vpeId = vdsTestUtil.getVpeById();

        assertThat(vpeId.getIdentity()).isNotEmpty();
    }
}
