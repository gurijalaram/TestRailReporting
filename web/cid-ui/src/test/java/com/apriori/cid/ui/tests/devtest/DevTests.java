package com.apriori.cid.ui.tests.devtest;

import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dto.ComponentRequestUtil;
import com.apriori.shared.util.models.response.component.PostComponentResponse;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DevTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {14726, 15015})
    @Description("Attempt to edit more than 10 scenarios")
    public void testEditMoreThanTenScenarios() {
        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(11);

        loginPage = new CidAppLoginPage(driver);
        PostComponentResponse componentItems = loginPage.login(components.get(0).getUser())
            .uploadMultiComponentsCID(components);

        softAssertions.assertThat(componentItems.getSuccesses()).hasSize(11);

        componentItems.getSuccesses().forEach(component -> softAssertions.assertThat(component.getComponentIdentity()).isNotEmpty());

        explorePage = new ExplorePage(driver);
        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
