package com.apriori.utils;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

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
import org.openqa.selenium.ScriptTimeoutException;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author kpatel
 */
public class PageUtils {

    public static final int BASIC_WAIT_TIME_IN_SECONDS = 60;
    static final Logger logger = LoggerFactory.getLogger(PageUtils.class);
    private static PageUtils instance = null;
    private WebDriver driver;
    private List<Class<? extends WebDriverException>> ignoredWebDriverExceptions = Arrays.asList(NoSuchElementException.class, ElementClickInterceptedException.class,
        StaleElementReferenceException.class, ElementNotInteractableException.class);
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

    public boolean isPageLoaded(WebElement element) {
        waitForElementToAppear(element);
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

    public void jsNewTab() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.open()");
    }

    /**
     * Use javascript to get the parent node of the element
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement jsGetParentElement(WebElement element) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript(
            "return arguments[0].parentElement", element);
    }

    /**
     * Uses actions to click an element
     *
     * @param targetElement - the element
     */
    public void actionClick(WebElement targetElement) {
        Actions builder = new Actions(driver);

        try {
            builder.click(targetElement).build().perform();
        } catch (StaleElementReferenceException | TimeoutException e) {
            builder.click(targetElement).build().perform();
        }
    }

    /**
     * Sets the value attribute to empty string
     *
     * @param targetElement - web element
     */
    public void clearInput(WebElement targetElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = ''", targetElement);
    }

    /**
     * Clears a text input area
     *
     * @param element - the webelement
     */
    public void clear(WebElement element) {
        waitForElementAndClick(element);
        element.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
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
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(" + scrollDown + ");", element);
        return element;
    }

    /**
     * Wait until element is on top and interactable
     *
     * @param locator - the locator
     * @return webelement
     */
    public WebElement waitForSteadinessOfElement(By locator) {
        int count = 0;
        while (count < 12) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 12);
                return wait.until(steadinessOfElementLocated(locator, true));
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

    /**
     * Checks the elements is displayed by size
     *
     * @param element - the element
     * @return int
     */
    public int waitForElementsToAppear(By element) {
        long startTime = System.currentTimeMillis() / 1000;

        int secondsToWait = 1;
        try {
            do {
                TimeUnit.SECONDS.sleep(secondsToWait);
                driver.findElements(element);
            } while (driver.findElements(element).size() < 1 && ((System.currentTimeMillis() / 1000) - startTime) < BASIC_WAIT_TIME_IN_SECONDS);

            return driver.findElements(element).size();

        } catch (StaleElementReferenceException | InterruptedException e) {
            logger.debug("Trying to recover from a stale element reference exception");
        }
        throw new AssertionError("Element is not displayed");
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public int waitForElementsToNotAppear(By element) {
        long startTime = System.currentTimeMillis() / 1000;
        long maxWaitTime = 120L;
        int elementSize = 0;

        try {
            logger.info(String.format("Waiting for element '%s' to be invisible", element));
            do {
                elementSize = driver.findElements(element).size();
            } while (elementSize > 0 && ((System.currentTimeMillis() / 1000) - startTime) < maxWaitTime);

            if (elementSize > 0) {
                throw new RuntimeException(String.format("Element '%s' should not be visible after %ssecs", element, maxWaitTime));
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return elementSize;
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public int waitForElementsToNotAppear(By element, long timeoutInMinutes) {
        long startTime = System.currentTimeMillis() / 1000;
        long maxWaitTime = 120L * timeoutInMinutes;
        int elementSize = 0;

        try {
            logger.info(String.format("Waiting for element '%s' to be invisible", element));
            do {
                elementSize = driver.findElements(element).size();
            } while (elementSize > 0 && ((System.currentTimeMillis() / 1000) - startTime) < maxWaitTime);

            if (elementSize > 0) {
                throw new RuntimeException(String.format("Element '%s' should not be visible after %ssecs", element, maxWaitTime));
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return elementSize;
    }

    /**
     * Checks element is displayed
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementAppear(WebElement element) {
        long maxWaitTime = 120L;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));

            return new WebDriverWait(driver, maxWaitTime)
                .ignoreAll(ignoredWebDriverExceptions)
                .until(visibilityOf(element));

        } catch (NoSuchElementException | StaleElementReferenceException | ElementNotInteractableException | ScriptTimeoutException | TimeoutException e) {
            throw new RuntimeException(String.format("Element '%s' was not displayed after %ssecs", element, maxWaitTime));
        }
    }

    /**
     * Checks element is displayed
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToAppear(WebElement element) {
        long maxWaitTime = 120L;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));

            return new WebDriverWait(driver, maxWaitTime)
                .ignoreAll(ignoredWebDriverExceptions)
                .until(visibilityOf(element));

        } catch (NoSuchElementException | StaleElementReferenceException | ElementNotInteractableException | ScriptTimeoutException | TimeoutException e) {
            throw new RuntimeException(String.format("Element '%s' was not displayed after %ssecs", element, maxWaitTime));
        }
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public WebElement waitForElementToAppear(By element) {
        long startTime = System.currentTimeMillis() / 1000;
        long maxWaitTime = 120L;
        int elementSize;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));
            do {
                elementSize = driver.findElements(element).size();
            } while (elementSize < 1 && ((System.currentTimeMillis() / 1000) - startTime) < maxWaitTime);

            if (elementSize < 1) {
                throw new RuntimeException(String.format("Element '%s' was not displayed after %ssecs", element, maxWaitTime));
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return driver.findElement(element);
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public List<WebElement> waitForElementsToAppear(List<WebElement> element) {
        long maxWaitTime = 120L;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));

            return new WebDriverWait(driver, maxWaitTime)
                .ignoreAll(ignoredWebDriverExceptions)
                .until(visibilityOfAllElements(element));

        } catch (NoSuchElementException | StaleElementReferenceException | ElementNotInteractableException | TimeoutException e) {
            throw new RuntimeException(String.format("Element '%s' was not displayed after %ssecs", element, maxWaitTime));
        }
    }

    /**
     * Wait for element to be clickable
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        long maxWaitTime = 120L;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));

            return new WebDriverWait(driver, maxWaitTime)
                .ignoreAll(ignoredWebDriverExceptions)
                .until(elementToBeClickable(element));

        } catch (NoSuchElementException | StaleElementReferenceException | ElementNotInteractableException | TimeoutException e) {
            throw new RuntimeException(String.format("Element '%s' was not clickable after %ssecs", element, maxWaitTime));
        }
    }

    /**
     * Wait for element to be clickable
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToBeClickable(By element) {
        long maxWaitTime = 120L;

        try {
            logger.info(String.format("Attempting to locate element '%s'", element));

            return new WebDriverWait(driver, maxWaitTime)
                .ignoreAll(ignoredWebDriverExceptions)
                .until(elementToBeClickable(element));

        } catch (NoSuchElementException | StaleElementReferenceException | ElementNotInteractableException | TimeoutException e) {
            throw new RuntimeException(String.format("Element '%s' was not clickable after %ssecs", element, maxWaitTime));
        }
    }

    /**
     * Ignores exceptions, waits for the element to be clickable then clicks it
     *
     * @param locator - the locator of the element
     */
    public void waitForElementAndClick(WebElement locator) {
        waitForElementToBeClickable(locator);
        locator.click();
    }

    /**
     * Ignores exceptions, waits for the element to be clickable then clicks it
     *
     * @param locator - the locator of the element
     */
    public void waitForElementAndClick(By locator) {
        waitForElementToBeClickable(locator);
        driver.findElement(locator).click();
    }

    /**
     * Waits for the element to be invisible
     *
     * @param locator - the locator of the element
     * @return true/false
     */
    public boolean invisibilityOfElements(List<WebElement> locator) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS * 2;

        return new WebDriverWait(driver, timeoutInMinutes)
            .withMessage("\nNot expecting: " + locator)
            .ignoreAll(ignoredWebDriverExceptions)
            .until(ExpectedConditions.invisibilityOfAllElements(locator));
    }

    /**
     * @param element The element to check is clickable
     * @return True if the element is clickable
     */
    public boolean isElementClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Finds element in a table by scrolling.
     *
     * @param scenario - the locator for the scenario
     * @param scroller - the scroller to scroll the element into view
     * @return - the element as a webelement
     */
    public WebElement scrollToElement(By scenario, WebElement scroller, String keyboardButton) {
        long startTime = System.currentTimeMillis() / 1000;
        int count = 0;
        Keys keyAction = null;
        switch (keyboardButton) {
            case "page_down":
                keyAction = Keys.PAGE_DOWN;
                break;
            case "arrow_down":
                keyAction = Keys.DOWN;
                break;
            case "horizontal_scroll":
                keyAction = Keys.RIGHT;
                break;
            default:
                logger.error("unknown/no scroll action found");
                break;
        }

        while (count < 12) {
            try {
                if (scroller.isDisplayed() && !isElementDisplayed(scenario)) {
                    do {
                        scroller.sendKeys(keyAction);
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
    public List<WebElement> scrollToElements(By scenario, WebElement scroller, String keyboardButton) {
        long startTime = System.currentTimeMillis() / 1000;
        int count = 0;
        Keys keyboardAction = keyboardButton.equals("page_down") ? Keys.PAGE_DOWN : Keys.DOWN;

        while (count < 12) {
            try {
                if (scroller.isDisplayed() && !isElementDisplayed(scenario)) {
                    do {
                        scroller.sendKeys(keyboardAction);
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
    public void waitForElementNotDisplayed(WebElement locator, int timeoutInMinutes) {
        new WebDriverWait(driver, (BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes))
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
    public boolean textPresentInElement(WebElement locator, String text) {
        boolean textPresent;

        try {

            textPresent = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS)
                .withMessage("\nExpected: " + text.replace("\n", " ") + "\nFound: " + waitForElementToAppear(locator).getText())
                .ignoreAll(ignoredWebDriverExceptions)
                .until((ExpectedCondition<Boolean>) element -> (waitForElementToAppear(locator)).getText().contains(text));

        } catch (TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
        return textPresent;
    }

    /**
     * Checks for string to not be present in element text and returns true/false
     *
     * @param locator - the locator of the element
     * @param text
     * @return true/false
     */
    public boolean textNotPresentInElement(WebElement locator, String text, long timeoutInMinutes) {
        boolean textPresent;

        try {

            textPresent = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes)
                .withMessage("\nNot expecting: " + text + "\nFound: " + waitForElementToAppear(locator).getText())
                .ignoreAll(ignoredWebDriverExceptions)
                .until(not((ExpectedCondition<Boolean>) element -> (waitForElementToAppear(locator)).getText().contains(text)));

        } catch (TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
        return textPresent;
    }

    /**
     * Waits for the element and check attribute is empty
     *
     * @param locator - the locator of the element
     * @return
     */
    public boolean checkElementAttributeEmpty(WebElement locator, String attribute) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS / 2;

        return new WebDriverWait(driver, timeoutInMinutes)
            .withMessage("\nExpected attribute: " + attribute + "\t" + "\nFound: " + locator.getAttribute(attribute))
            .until((ExpectedCondition<Boolean>) element -> (locator).getAttribute(attribute).isEmpty());
    }

    /**
     * Checks the element's size on the page is greater than 0 and returns true/false
     *
     * @param locator - the element as list
     * @return true/false
     */
    public <T> boolean checkElementVisibleByBoolean(List<T> locator) {
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS * 2;

        return new WebDriverWait(driver, timeoutInMinutes)
            .withMessage("\nElement not visible using locator: " + locator)
            .until((ExpectedCondition<Boolean>) element -> (locator).size() > 0);
    }

    /**
     * Waits for the element's specified attribute to contain the specified text
     *
     * @param locator   - element to get attribute of
     * @param attribute - attribute to get from element
     * @param text      - expected value
     * @return - boolean
     */
    public boolean checkElementAttribute(WebElement locator, String attribute, String text) {
        final int timeOut = BASIC_WAIT_TIME_IN_SECONDS / 2;

        return new WebDriverWait(driver, timeOut)
            .withMessage("\nExpected: " + text + "\t" + "\nFound: " + locator.getAttribute(attribute))
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
            .withMessage("\nExpected option not in dropdown: " + text + "\nLocator: " + locator)
            .ignoreAll(ignoredWebDriverExceptions)
            .until((ExpectedCondition<Boolean>) element -> (new Select(locator)).getFirstSelectedOption().getText().equalsIgnoreCase(text));
    }

    /**
     * Waits for the dropdown to be populated with the options
     *
     * @param locator - the locator
     * @param option  - the option
     */
    public void checkDropdownOptions(WebElement locator, String option) {
        new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 2)
            .withMessage("\nExpected option not in dropdown: " + option + "\nLocator: " + locator)
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
     * todo - this is a WIP and will be developed further in the future
     *
     * @return webdriver functions
     */
    public WebDriver windowHandler(int index) {
        List<String> windowList = new ArrayList<>(driver.getWindowHandles());
        return driver.switchTo().window(windowList.get(index));
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
     * Gets tab two URL
     *
     * @return String
     */
    public String getTabTwoUrl() {
        return windowHandler(1).getCurrentUrl();
    }

    /**
     * Gets URL to assert against
     *
     * @return String
     */
    public String getUrlToCheck() {
        return System.getProperty("baseUrl");
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param value            - the value
     * @return current page object
     */
    public void typeAheadSelect(WebElement dropdownSelector, String value) {
        waitForElementToAppear(dropdownSelector);
        waitForElementAndClick(dropdownSelector);
        By byValue = By.xpath(String.format("//div[.='%s']//div[@id]", value));
        waitForElementToAppear(byValue);
        waitForElementAndClick(byValue);
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param locatorId        - the locator id
     * @param locatorValue     - the locator value
     * @return current page object
     */
    public void typeAheadSelect(WebElement dropdownSelector, String locatorId, String locatorValue) {
        waitForElementToAppear(dropdownSelector);
        waitForElementAndClick(dropdownSelector);
        By byValue = By.xpath(String.format("//div[@id='%s']//div[.='%s']//div[@id]", locatorId, locatorValue));
        waitForElementToAppear(byValue);
        waitForElementAndClick(byValue);
    }

    /**
     * Click on an element that is off screen. The element in this case would be marked as un-clickable.
     * This method brings the element into focus without having to scroll
     *
     * @param element Element to click on
     */
    public void clickOnOffScreenElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("return arguments[0].click();", element);
    }

    /**
     * Drag and drop an element from source to target
     *
     * @param byElementSource - element source
     * @param byElementTarget - element target
     */
    public void dragAndDrop(WebElement byElementSource, WebElement byElementTarget) {
        Actions actions = new Actions(driver);
        actions.clickAndHold(byElementSource)
            .pause(Duration.ofMillis(100))
            .moveByOffset(0, -5)
            .moveByOffset(0, -5)
            .moveToElement(byElementTarget)
            .pause(Duration.ofMillis(500))
            .release()
            .pause(Duration.ofSeconds(1))
            .perform();
    }
}