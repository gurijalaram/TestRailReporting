package com.apriori.explore;

import static com.utils.ColumnsEnum.LOGISTICS_CARBON;
import static com.utils.ColumnsEnum.MATERIAL_CARBON;
import static com.utils.ColumnsEnum.PROCESS_CARBON;

import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.DirectionEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SustainabilityFieldsTests extends TestBaseUI {
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = 24429)
    @Description("Verify sustainability fields In explore view")
    public void sustainabilityFieldsInExploreView() {
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .configure()
            .selectColumn(MATERIAL_CARBON)
            .selectColumn(PROCESS_CARBON)
            .selectColumn(LOGISTICS_CARBON)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        List<String> headers = explorePage.getTableHeaders();
        softAssertions.assertThat(headers).contains("Material Carbon","Process Carbon","Logistics Carbon");
        softAssertions.assertAll();
    }
}
