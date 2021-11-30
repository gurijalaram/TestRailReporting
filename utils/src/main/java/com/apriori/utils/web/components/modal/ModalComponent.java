package com.apriori.utils.web.components.modal;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.CommonComponent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents a component that is opened in a modal.
 *
 * Modals are opened on the body tag.  You CAN attach this using
 * a web element if you query for it yourself, but generally, you're
 * best bet is to use the default constructor or the constructor
 * with the class name to target the modal in the appropriate spot.
 */
public abstract class ModalComponent<T extends ModalComponent<T>> extends CommonComponent {
    private static final By BY_HEADER = By.className("modal-header");
    private static final By BY_BODY = By.className("modal-body");
    private static final By BY_FOOTER = By.className("modal-footer");
    private static final By BY_CLOSE = By.className("close");

    /**
     * Queries for a modal by the class name.
     *
     * @param driver The web driver
     * @param klass The class name to query by.  This will query underneath the body.
     */
    public ModalComponent(final WebDriver driver, final String klass) {
        this(driver, driver.findElement(By.cssSelector(String.format("body .%s", klass))));
    }

    /**
     * @inheritDoc
     */
    public ModalComponent(final WebDriver driver, final WebElement element) {
        super(driver, element);
    }

    /**
     * Gets the header element.
     *
     * @return The element under the modal that roots the header.
     */
    protected WebElement getHeaderRoot() {
        return getPageUtils().waitForElementToAppear(BY_HEADER, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets the body element.
     *
     * @return The element under the modal that roots the body.
     */
    protected WebElement getBodyRoot() {
        return getPageUtils().waitForElementToAppear(BY_BODY, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets the footer element.
     *
     * @return The element under the modal that roots the footer.
     */
    protected WebElement getFooterRoot() {
        return getPageUtils().waitForElementToAppear(BY_FOOTER, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Clicks an element in the footer.
     *
     * @param by The search query.
     */
    protected final void clickFooterElement(By by) {
        WebElement element = getPageUtils().waitForElementToAppear(by, PageUtils.DURATION_FAST, getFooterRoot());
        getPageUtils().waitForElementAndClick(element);
    }

    /**
     * Clicks the close button in the header.
     */
    protected final void clickCloseButtonInHeader() {
        WebElement closeButton = getPageUtils().waitForElementToAppear(BY_CLOSE, PageUtils.DURATION_INSTANT, getHeaderRoot());
        getPageUtils().waitForElementAndClick(closeButton);
    }

    /**
     * Gets the text from the body element.
     *
     * This is useful if you have a basic message component and you
     * want to validate that message text.
     *
     * @return The body text.
     */
    public final String getBodyText() {
        return getBodyRoot().getText();
    }
}
