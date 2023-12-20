package com.apriori.cid.ui.tests.evaluate.materialutilization;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.settings.ProductionDefaultsPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class MaterialPMITests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentInfoBuilder component;

    public MaterialPMITests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6283, 5917})
    @Description("Test setting a default material and ensure parts are costed in that material by default")
    public void materialTestProductionDefault() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_BRAZIL)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_6061.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(3);

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_6061.getMaterialName()), is(true));
    }
}
