package com.apriori.pageobjects.utils;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpatel
 */
public class PageUtils {

    private WebDriver driver;
    public static final int BASIC_WAIT_TIME_IN_SECONDS = 60;
    private List<Class<? extends WebDriverException>> ignoredWebDriverExceptions = Arrays.asList(NoSuchElementException.class, ElementClickInterceptedException.class,
        StaleElementReferenceException.class, ElementNotInteractableException.class);

    static final Logger logger = LoggerFactory.getLogger(PageUtils.class);
    protected static final Logger steps_logger = LoggerFactory.getLogger("steps_logger");

    private String currentlyOn = "CURRENTLY_ON_PAGE:";

    public PageUtils(WebDriver driver) {
        this.driver = driver;
    }

    public String currentlyOnPage(String pageName) {
        return currentlyOn + " " + pageName;
    }

    // TODO not a wait method
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean isAlertPresent(String alertText) {
        try {
            Alert alert = driver.switchTo().alert();
            return alert.getText().equalsIgnoreCase(alertText);
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    /**
     * Check if the element is present in the DOM or not. Important: this is not a wait method, it
     * only shows the current status of the element.
     *
     * @param by By object
     * @return boolean whether element is present or not
     */
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // TODO not a wait method
    public boolean isSubElementPresent(WebElement element, By locator) {
        try {
            element.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if the element is displayed or not. Important: this is not a wait method, it only shows
     * the current status of the element.
     *
     * @param by - By object
     * @return returns whether element is visible or not
     */
    public boolean isElementDisplayed(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if the element is displayed or not. Important: this is not a wait method, it only shows
     * the current status of the element.
     *
     * @param element - WebElement
     * @return - returns whether element is displayed or not
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Check if the sub element is displayed or not. Important: this is not a wait method, it only
     * shows the current status of the element.
     *
     * @param parent - parent element
     * @param by     - by
     * @return boolean whether sub-eleent id displayed or not
     */
    public boolean isSubElementDisplayed(WebElement parent, By by) {
        try {
            return parent.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if the element is enabled or not. Important: this is not a wait method, it only shows
     * the current status of the element.
     *
     * @param element - WebElement
     * @return - returns whether element is enabled or not
     */
    public boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isPageLoaded() {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
    }

    public void mouseMove(WebElement element) {
        Actions act = new Actions(driver);
        act.moveToElement(element).build().perform();
    }

    public void mouseMoveWithOffsets(WebElement element, int offsetX, int offsetY) {
        Actions act = new Actions(driver);
        act.moveToElement(element, offsetX, offsetY).build().perform();
    }

    public void mouseMoveWithOffsetsAndClick(WebElement element, int offsetX, int offsetY) {
        Actions act = new Actions(driver);
        act.moveToElement(element, offsetX, offsetY).click().build().perform();
    }

    public void moveAndClick(WebElement targetElement) {
        Actions builder = new Actions(driver);
        builder.moveToElement(targetElement).click(targetElement).perform();
    }

    public void javaScriptClick(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void actionClick(WebElement targetElement) {
        Actions builder = new Actions(driver);
        builder.click(targetElement).build().perform();
    }

    /**
     * Sets the value attribute to empty string
     *
     * @param targetElement - web element
     */
    public void clearInput(WebElement targetElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = ''", targetElement);
    }

    public Dimension getWindowDimension() {
        return driver.manage().window().getSize();
    }

    /**
     * @param scrollDown - true scrolls down and the element is visible on top of the page - false
     *                   scroll to the top of the page (try to scroll as high that the given element is in the bottom
     *                   of the screen)
     */
    public WebElement scrollWithJavaScript(WebElement element, boolean scrollDown) {
        steps_logger.debug("Scroll down");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(" + scrollDown + ");", element);
        // waitFor(500);
        return element;
    }

    /**
     * Internal wait implementation for the function waitForElementToStop() It waits for the
     * elements to stop moving also checking if it is displayed and is it on top.
     *
     * @param locator
     * @param onTop
     * @return
     */
    private ExpectedCondition<WebElement> steadinessOfElementLocated(final By locator, final boolean onTop) {
        return new ExpectedCondition<WebElement>() {

            private WebElement element = null;
            private Point point = null;

            @Override
            public WebElement apply(WebDriver driver) {
                if (element == null) {
                    try {
                        element = driver.findElement(locator);
                    } catch (NoSuchElementException e) {
                        logger.debug("Element not found");
                        return null;
                    }
                }

                try {
                    if (element.isDisplayed()) {
                        Point location = element.getLocation();
                        if (location.equals(point) && (!onTop || isOnTop(element))) {
                            return element;
                        }
                        point = location;
                    }
                } catch (StaleElementReferenceException e) {
                    element = null;
                }

                return null;
            }

            @Override
            public String toString() {
                return "steadiness of element located by " + locator;
            }
        };
    }

    /**
     * Helper function which checks if the element is on Top
     *
     * @param element
     * @return
     */
    private boolean isOnTop(WebElement element) {

        return (boolean) ((JavascriptExecutor) driver).executeScript(
            "var elm = arguments[0];" +
                "var doc = elm.ownerDocument || document;" +
                "var rect = elm.getBoundingClientRect();" +
                "return elm === doc.elementFromPoint(rect.left + (rect.width / 2), rect.top + (rect.height / 2));",
            element);
    }

    /**
     * Waits for a given time
     *
     * @param millis - time in milliseconds
     */
    public void waitFor(Integer millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
    }

    public WebElement waitForElementToAppear(By locator) {
        return waitForElementToAppear(locator, "Element did not appear");
    }

    public WebElement waitForElementToAppear(By locator, String message) {
        return waitForAppear(ExpectedConditions.visibilityOfElementLocated(locator), message);
    }

    public WebElement waitForElementToAppear(WebElement element) {
        return waitForAppear(visibilityOf(element), "Element did not appear");
    }

    public WebElement waitForElementToAppear(WebElement element, String message) {
        return waitForAppear(visibilityOf(element), message);
    }

    public WebElement waitForElementToAppear(WebElement locator, int timeoutInMinutes) {
        return waitForAppear(visibilityOf(locator), "Element did not appear", timeoutInMinutes);
    }

    public WebElement waitForElementToAppear(By locator, int timeoutInMinutes) {
        return waitForAppear(ExpectedConditions.visibilityOfElementLocated(locator), "Element did not appear", timeoutInMinutes);
    }

    public List<WebElement> waitForElementsToAppear(List<WebElement> elements) {
        return waitForAppear(ExpectedConditions.visibilityOfAllElements(elements), "Elements did not appear");
    }

    public WebElement waitForElementAppear(WebElement element) {
        return waitForAppear(element);
    }

    private WebElement waitForAppear(WebElement element) {
        return new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS)
            .ignoreAll(ignoredWebDriverExceptions)
            .until(visibilityOf(element));
    }

    private <T> T waitForAppear(ExpectedCondition<T> condition, String message) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 12);
                return wait.until(condition);
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            }
        }
        throw new AssertionError(message + ": " + condition);
    }

    private <T> T waitForAppear(ExpectedCondition<T> condition, String message, int timeoutInMinutes) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes);
                return wait.until(condition);
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            }
        }
        throw new AssertionError(message + ": " + condition);
    }

    public WebElement waitForChildElementToAppear(WebElement parentElement, By childLocator) {
        int count = 0;
        while (count < 20) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 20);
                return wait.until(visibilityOf(parentElement.findElement(childLocator)));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
                logger.debug("TimeoutException {}x", count);
            } catch (NoSuchElementException e) {
                count = count + 1;
                logger.debug("NoSuchElementException {}x", count);
                waitFor((BASIC_WAIT_TIME_IN_SECONDS / 20) * 1000);
            }
        }
        throw new AssertionError("Element did not appear: " + childLocator);
    }

    public WebElement waitForChildElementToAppearWithCustomWaitTime(WebElement parentElement, By childLocator, int waitTimeInSecond) {
        int count = 0;
        while (count < 2) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, waitTimeInSecond / 2);
                return wait.until(visibilityOf(parentElement.findElement(childLocator)));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            } catch (NoSuchElementException e) {
                count = count + 1;
                logger.debug("NoSuchElementException {}x", count);
                waitFor((waitTimeInSecond / 2) * 1000);
            }
        }
        throw new AssertionError("Element did not appear: " + childLocator);
    }

    public WebElement waitForElementToBeClickable(By locator) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 12);
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            }
        }

        throw new AssertionError("Element is not clickable: " + locator);
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 12);
                return wait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            }
        }
        throw new AssertionError("Element is not clickable: " + element);
    }

    public WebElement waitForElementToBeClickable(WebElement parentElement, By childLocator) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 12);
                return wait.until(ExpectedConditions.elementToBeClickable(parentElement.findElement(childLocator)));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException | NoSuchElementException e) {
                count = count + 1;
            }
        }
        throw new AssertionError("Element did not appear: " + childLocator);
    }

    public WebElement waitForElementToBeClickable(WebElement element, int timeOutInMinutes) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS * timeOutInMinutes);
                return wait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (StaleElementReferenceException e) {
                // e.toString();
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException e) {
                count = count + 1;
            }
        }
        throw new AssertionError("Element is not clickable: " + element);
    }

    /**
     * Finds element in a table by scrolling.
     *
     * @param scenario - the locator for the scenario
     * @param scroller - the scroller to scroll the element into view
     * @return - the element as a webelement
     */
    public WebElement scrollToElement(By scenario, WebElement scroller) {
        long startTime = System.currentTimeMillis() / 1000;
        int count = 0;

        while (count < 12) {
            try {
                if (scroller.isDisplayed() && driver.findElements(scenario).size() < 1) {
                    do {
                        scroller.sendKeys(Keys.PAGE_DOWN);
                    } while (driver.findElements(scenario).size() < 1 && ((System.currentTimeMillis() / 1000) - startTime) < BASIC_WAIT_TIME_IN_SECONDS * 2);

                    Coordinates processCoordinates = ((Locatable) driver.findElement(scenario)).getCoordinates();
                    processCoordinates.inViewPort();

                    return driver.findElement(scenario);
                } else {
                    return driver.findElement(scenario);
                }
            } catch (ElementNotInteractableException e) {
                logger.debug("Trying to recover from an element not interactable exception");
                count = count + 1;
            } catch (NoSuchElementException e) {
                logger.debug("Trying to recover from no such element exception");
                count = count + 1;
            } catch (StaleElementReferenceException e) {
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            }
        }
        return driver.findElement(scenario);
    }

    /**
     * Finds elements in a table by scrolling.
     *
     * @param scenario - the locator for the scenario
     * @param scroller - the scroller to scroll the element into view
     * @return - the element as a webelement
     */
    public List<WebElement> scrollToElements(By scenario, WebElement scroller) {
        long startTime = System.currentTimeMillis() / 1000;
        int count = 0;

        while (count < 12) {
            try {
                if (scroller.isDisplayed() && driver.findElements(scenario).size() < 1) {
                    do {
                        scroller.sendKeys(Keys.PAGE_DOWN);
                    } while (driver.findElements(scenario).size() < 1 && ((System.currentTimeMillis() / 1000) - startTime) < BASIC_WAIT_TIME_IN_SECONDS * 2);

                    return driver.findElements(scenario);
                } else {
                    return driver.findElements(scenario);
                }
            } catch (ElementNotInteractableException e) {
                logger.debug("Trying to recover from an element not interactable exception");
                count = count + 1;
            } catch (StaleElementReferenceException e) {
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            }
        }
        return driver.findElements(scenario);
    }

    /**
     * Checks the element's size on the page is less than 1 and returns true/false
     *
     * @param locator - the element as list
     * @return true/false
     */
    public <T> boolean checkElementsNotVisibleByBoolean(List<T> locator) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS / 6;
        try {
            return new WebDriverWait(driver, timeoutInMinutes)
                .until((ExpectedCondition<Boolean>) element -> (locator).size() < 1);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        throw new AssertionError("\nWaited for: " + timeoutInMinutes + "(s)\nElement : " + locator + " is not visible");
    }

    /**
     * Selects the correct option in the dropdown.  Conditional statement is included because the system
     * tends to revert to previous selection.
     *
     * @param locator        - the locator of the element
     * @param dropdownOption - the dropdown option
     */
    public void selectDropdownOption(WebElement locator, String dropdownOption) {
        new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((WebDriver driver) -> {
                new Select(locator).selectByVisibleText(dropdownOption);
                if (!new Select(locator).getFirstSelectedOption().equals(dropdownOption)) {
                    new Select(locator).selectByVisibleText(dropdownOption);
                }
                return true;
            });
    }

    /**
     * Waits for the element to become disabled
     *
     * @param locator - the locator of the element
     * @return
     */
    public void waitForElementNotDisplayed(WebElement locator, double timeoutInMinutes) {
        new WebDriverWait(driver, (long) (BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(not((ExpectedCondition<Boolean>) element -> (locator).isDisplayed()));
    }

    /**
     * Checks for string to be present in element text and returns true/false
     *
     * @param locator - the locator of the element
     * @param text
     * @return true/false
     */
    public boolean checkElementContains(WebElement locator, String text) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS / 2;
        try {
            return new WebDriverWait(driver, timeoutInMinutes)
                .ignoreAll(ignoredWebDriverExceptions)
                .until((ExpectedCondition<Boolean>) element -> (locator).getText().contains(text));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        throw new AssertionError("\nWaited for: " + timeoutInMinutes + "(s)\nExpected: " + text + "\nFound: " + locator.getText());
    }

    /**
     * Checks for string to not be present in element text and returns true/false
     *
     * @param locator - the locator of the element
     * @param text
     * @return true/false
     */
    public boolean checkElementNotContain(WebElement locator, String text, int timeoutInMinutes) {
        return new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes)
            .ignoreAll(ignoredWebDriverExceptions)
            .until(not((ExpectedCondition<Boolean>) element -> (locator).getText().contains(text)));
    }

    /**
     * Ignores exceptions and waits for the element to be clickable
     *
     * @param locator - the locator of the element
     */
    public void waitForElementAndClick(WebElement locator) {
        waitForElementToBeClickable(locator);
        new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 2)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((WebDriver webDriver) -> {
                locator.click();
                return true;
            });
    }

    /**
     * Ignores exceptions and waits for the element to be clickable
     *
     * @param locator - the locator of the element
     */
    public void waitForElementAndClick(By locator) {
        waitForElementToBeClickable(locator);
        new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 2)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((WebDriver webDriver) -> {
                driver.findElement(locator).click();
                return true;
            });
    }

    /**
     * Waits for the element and check attribute is empty
     *
     * @param locator - the locator of the element
     * @return
     */
    public boolean checkElementAttributeEmpty(WebElement locator, String attribute) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS / 2;
        try {
            return new WebDriverWait(driver, timeoutInMinutes)
                .until((ExpectedCondition<Boolean>) element -> (locator).getAttribute(attribute).isEmpty());
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        throw new AssertionError("\nWaited for: " + timeoutInMinutes + "(s)\nLocator: " + locator + " does not contain attribute: " + attribute);
    }

    /**
     * Checks the element's size on the page is greater than 0 and returns true/false
     *
     * @param locator - the element as list
     * @return true/false
     */
    public <T> Boolean checkElementVisibleByBoolean(List<T> locator) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS * 2;
        try {
            return new WebDriverWait(driver, timeoutInMinutes)
                .until((ExpectedCondition<Boolean>) element -> (locator).size() > 0);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        throw new AssertionError("\nWaited for: " + timeoutInMinutes + "(s)\nElement : " + locator + " is not visible");
    }

    /**
     * Waits for the element's specified attribute to contain the specified text
     *
     * @param - element to get attribute of
     * @param - attribute to get from element
     * @param - expected value
     * @return - boolean
     */
    public boolean checkElementAttribute(WebElement locator, String attribute, String text) {
        final int timeOut = BASIC_WAIT_TIME_IN_SECONDS / 2;

        return new WebDriverWait(driver, timeOut)
            .withMessage("\nExpected: " + text + "\nFound: " + locator.getAttribute(attribute))
            .ignoreAll(ignoredWebDriverExceptions)
            .until((ExpectedCondition<Boolean>) element -> (locator).getAttribute(attribute).contains(text));
    }

    /**
     * Waits for the element and checks the first selected option
     *
     * @param locator - the locator of the element
     * @return
     */
    public boolean checkElementFirstOption(WebElement locator, String text) {
        return new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 2)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((ExpectedCondition<Boolean>) element -> (new Select(locator)).getFirstSelectedOption().getText().equalsIgnoreCase(text));
    }

    /**
     * Waits the dropdown to be populated with the options
     *
     * @param locator - the locator
     * @param option  - the option
     */
    public void checkDropdownOptions(WebElement locator, String option) {
        new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 2)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((ExpectedCondition<Boolean>) element -> (new Select(locator).getOptions().stream().anyMatch(dropdownOptions -> dropdownOptions.getText().contains(option))));
    }

    /**
     * Moves the scroller up
     *
     * @param scroller           - the scroller
     * @param timeLimitInSeconds - the time limit in seconds to wait
     */
    public void scrollUp(WebElement scroller, int timeLimitInSeconds) {
        long startTime = System.currentTimeMillis() / 1000;
        if (!isElementDisplayed(scroller)) {
            return;
        } else {
            do {
                scroller.sendKeys(Keys.UP);
            } while (((System.currentTimeMillis() / 1000) - startTime) < timeLimitInSeconds);
        }
    }

    /**
     * Gets a list of current windows and switches to the first child window only
     * todo - this is a WIP and will be developed further in the further
     *
     * @return webdriver functions
     */
    public WebDriver windowHandler() {
        List<String> windowList = new ArrayList<>(driver.getWindowHandles());
        return driver.switchTo().window(windowList.get(1));
    }

    /**
     * Get link response code
     *
     * @param linkURL - URL of link
     * @return String response code
     */
    public int urlRespCode(String linkURL) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(linkURL).openConnection());
        httpURLConnection.setRequestMethod("HEAD");
        httpURLConnection.connect();
        return httpURLConnection.getResponseCode();
    }

    /**
     * Gets count of open tabs
     *
     * @return int - number of open tabs
     */
    public int getCountOfOpenTabs() {
        return driver.getWindowHandles().size();
    }

    /**
     * Gets text of an element
     *
     * @param element WebElement
     * @return String heading
     */
    public String getElementText(WebElement element) {
        return element.getText();
    }

    /**
     * Gets current URL
     *
     * @return String
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets tab two URL
     *
     * @return String
     */
    public String getTabTwoUrl() {
        return windowHandler().getCurrentUrl();
    }

    /**
     * Get name of a report
     *
     * @return String - text of report name
     */
    public String getReportNameText(String reportName) {
        By reportLinkLocator = By.xpath(String.format("//a[contains(text(), '%s')]", reportName));
        waitForElementToAppear(reportLinkLocator);
        return driver.findElement(reportLinkLocator).getText();
    }

    /**
     * Get report link element
     *
     * @return WebElement
     */
    public WebElement getReportLinkElement(String reportName) {
        By reportLinkLocator = By.xpath(String.format("//a[contains(text(), '%s')]", reportName));
        waitForElementToAppear(reportLinkLocator);
        return driver.findElement(reportLinkLocator);
    }
}