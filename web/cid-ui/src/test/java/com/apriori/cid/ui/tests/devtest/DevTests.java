package com.apriori.cid.ui.tests.devtest;

import com.apriori.cid.api.models.response.ComponentIdentityResponse;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DevTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {14726, 15015})
    @Description("Attempt to edit more than 10 scenarios")
    public void testEditMoreThanTenScenarios() {
        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT");
        final File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan charger lead.SLDPRT");
        final File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT");
        final File resourceFile4 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt");
        final File resourceFile5 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt");
        final File resourceFile6 = FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp");
        final File resourceFile7 = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp");
        final File resourceFile8 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt");
        final File resourceFile9 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0005b.ipt");
        final File resourceFile10 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt");
        final File resourceFile11 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt");

        loginPage = new CidAppLoginPage(driver);
        List<ComponentIdentityResponse> componentItems = loginPage.login(currentUser)
            .uploadMultiComponentsCID(Arrays.asList(resourceFile, resourceFile2, resourceFile3, resourceFile4, resourceFile5, resourceFile6, resourceFile7, resourceFile8, resourceFile9, resourceFile10, resourceFile11),
                scenarioName, currentUser);

        softAssertions.assertThat(componentItems.size()).isEqualTo(11);

        componentItems.forEach(component -> softAssertions.assertThat(component.getIdentity()).isNotEmpty());

        explorePage = new ExplorePage(driver);
        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}