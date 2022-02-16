package com.apriori.siteLicenses;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SelectComponent;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

@Slf4j
public class UploadLicensePopUp extends LoadableComponent<UploadLicensePopUp> {

    @FindBy(css = ".modal-title")
    private WebElement popUpHeader;

    @FindBy(css = ".btn.btn-secondary.mr-2")
    private WebElement cancelButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement loadButton;

    @FindBy(css = "input[name='customerNameConfirm']")
    private WebElement customerNameInput;

    @FindBy(className = "invalid-feedback-for-customer-name-confirm")
    private WebElement customerNameFeedback;

    @FindBy(css = "textarea[name='description']")
    private WebElement licenseNameInput;

    @FindBy(css = ".text-area-field-description .meta-form-group-error")
    private WebElement licenseNameFeedback;

    @FindBy(css = ".apriori-select.source-drop-down")
    private WebElement aPVersionDropDown;
    private SelectComponent aPVersionSelect;

    @FindBy(css = ".source-field-ap-version .meta-form-group-error")
    private WebElement aPVersionFeedback;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UploadLicensePopUp(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(popUpHeader);
    }

    /**
     * Validates fields in upload license popup
     *
     * @param labels - fields names
     * @param soft - soft assertions
     * @return - this object
     */
    public UploadLicensePopUp testUploadLicenseLabels(List<String> labels, SoftAssertions soft) {
        labels.forEach(label -> {
            List<WebElement> elements = driver.findElements(By.xpath(String.format("//label[.='%s']", label)));
            soft.assertThat(elements.size())
                    .overridingErrorMessage(String.format("Could not find the label, %s", label))
                    .isGreaterThan(0);
        });
        return this;
    }

    /**
     * Validates if load button is enabled
     *
     * @return true or false
     */
    public boolean isLoadButtonEnabled() {
        return pageUtils.isElementEnabled(loadButton);
    }

    /**
     * Fills in customer name field
     *
     * @param customerName - customer name
     * @return - this object
     */
    public UploadLicensePopUp enterCustomerName(String customerName) {
        pageUtils.setValueOfElement(customerNameInput, customerName);
        return this;
    }

    /**
     * Fills in license name field
     *
     * @param licenseName - the license name
     * @return - this object
     */
    public UploadLicensePopUp enterLicenseName(String licenseName) {
        pageUtils.setValueOfElement(licenseNameInput, licenseName);
        return this;
    }

    /**
     * Selects value in aP Version dropdown
     *
     * @param aPVersion - value of aP Version
     * @return - this object
     */
    public UploadLicensePopUp selectApVersion(String aPVersion) {
        this.aPVersionSelect = new SelectComponent(driver, aPVersionDropDown);
        aPVersionSelect.select(aPVersion);
        return this;
    }

    /**
     * Gets customer name field error
     *
     * @return string error message
     */
    public String getCustomerNameFeedback() {
        return this.customerNameFeedback.getText();
    }

    /**
     * Gets aP Version field error
     *
     * @return string error message
     */
    public String getApVersionFeedback() {
        return this.aPVersionFeedback.getText();
    }

    /**
     * Gets license name field error
     *
     * @return string error message
     */
    public String getLicenseNameFeedback() {
        return this.licenseNameFeedback.getText();
    }

    /**
     * Validates required fields in upload license popup
     *
     * @param soft - soft assertions
     * @return - this object
     */
    public UploadLicensePopUp testNecessaryFieldsAreRequired(SoftAssertions soft) {
        enterCustomerName(null);
        soft.assertThat(getCustomerNameFeedback())
            .isEqualTo("Please retype the customer name from the license file to confirm that this specific license file is intended for this customer.");
        selectApVersion(null);
        soft.assertThat(getApVersionFeedback())
            .isEqualTo("Please select aPriori Version. If there are no values - try to reload the page and try again.");
        enterLicenseName(null);
        soft.assertThat(getLicenseNameFeedback())
            .isEqualTo("Please provide an aPriori license name.");

        return this;
    }

    /**
     * Clicks cancel button
     *
     * @return new page object
     */
    public SitesLicensesPage clickCancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new SitesLicensesPage(driver);
    }
}