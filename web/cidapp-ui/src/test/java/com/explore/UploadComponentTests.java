package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.pageobjects.pages.explore.UploadedCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;

import java.io.File;

public class UploadComponentTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private UploadedCadFilePage cadFilePage;
    private FileUploadPage fileUploadPage;

    @Test
    @Category(SanityTests.class)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, ExplorePage.class)
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @Description("Get the success message after uploading Cad file")
    public void testUploadCadFile() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        fileUploadPage = loginPage.login(UserUtil.getUser())
            .uploadComponent(scenarioName, resourceFile);

        assertThat(fileUploadPage.scenarioNameTextBoxEnabled(), is(false));

        cadFilePage = fileUploadPage.submit(UploadedCadFilePage.class);

        assertThat(cadFilePage.getImportMessage(), is(containsString("1 file(s) Imported Successfully.")));

        cadFilePage.close();
    }
}
