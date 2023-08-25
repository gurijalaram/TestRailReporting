package com.apriori;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfAllElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.or;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import com.apriori.http.utils.Obligation;

import org.apache.commons.lang.StringUtils;
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
import org.openqa.selenium.SearchContext;
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
import java.util.function.Supplier;

/**
 * @author kpatel
 */
public class PageUtils {

    /**
     * A duration that is meant to be used when you just want to make sure the
     * UI is synchronized to the web element.  This is just a pince slower than
     * Duration.ZERO.
     */
    public static final Duration DURATION_INSTANT = Duration.ofMillis(10);
    /**
     * A duration that is meant to be used when you are expecting a component load that
     * may take a bit longer than an instant render.  This will always be under one second.
     */
    public static final Duration DURATION_FAST = Duration.ofMillis(100);
    /**
     * A duration that is meant to be used when you are expecting a component load that
     * may take longer to render than a simple input.
     */
    public static final Duration DURATION_SLOW = Duration.ofSeconds(1);
    /**
     * A duration that you should wait when you are waiting on a spinner.
     * <p>
     * If your component doesn't stabilize within this time frame, then it's time to consider that
     * there may be a performance problem.
     */
    public static final Duration DURATION_LOADING = Duration.ofSeconds(5);

    public static final int BASIC_WAIT_TIME_IN_SECONDS = 60;
    static final Logger logger = LoggerFactory.getLogger(PageUtils.class);
    private static PageUtils instance = null;
    private WebDriver driver;
    private List<Class<? extends WebDriverException>> ignoredWebDriverExceptions = Arrays.asList(NoSuchElementException.class, ElementClickInterceptedException.class,
        StaleElementReferenceException.class, ElementNotInteractableException.class);
    private String currentlyOn = "CURRENTLY ON PAGE:";

    public PageUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * highlights given element. This would be mainly used for debugging
     *
     * @param driver
     * @param element
     */
    public static void highlightElement(WebDriver driver, WebElement element) {

        // Original in Python: https://gist.github.com/3086536
        String originalStyle = element.getAttribute("style");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Failed to highlight element");
        }
        js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", element);
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

    /**
     * Determines if an element contains a class.
     *
     * @param element     The element that contains the class list to search.
     * @param wantedClass The desired class.
     * @return True if the element contains the wantedClass.  False otherwise.
     */
    public boolean doesElementHaveClass(WebElement element, String wantedClass) {
        String classList = element.getAttribute("class");
        classList = classList == null ? "" : classList;
        String[] classes = classList.split(" ");
        return Arrays.stream(classes).anyMatch((currentClass) -> StringUtils.equals(currentClass, wantedClass));
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

    public void javaScriptDelete(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].remove();", element);
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
        builder.click(waitForElementToAppear(targetElement)).build().perform();
    }

    /**
     * Uses actions to click an element
     *
     * @param targetElement - the element
     */
    public void actionClick(By targetElement) {
        Actions builder = new Actions(driver);
        builder.click(waitForElementToAppear(targetElement)).build().perform();
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
     * Clears the value of an element with a value attribute.
     * <p>
     * This method works on all platforms regardless of the OS modifier key
     * (CONTROL on Windows/Linux, COMMAND/META on OSX).
     *
     * @param elementWithValue The element that contains the value to delete.
     */
    public void clearValueOfElement(WebElement elementWithValue) {
        waitForElementAndClick(elementWithValue);

        // This little trick works on all OS's regardless of the command/control key.
        // Since the element is clicked in the middle in selenium, we have to make sure
        // the text is deleted before and after the caret.  Note here that the +1 just
        // makes it so that it sends at least one of each character in the case that
        // the current value is already empty.
        String currentValue = elementWithValue.getAttribute("value");
        Keys[] backspaces = new Keys[currentValue.length() + 1];
        Keys[] deletes = new Keys[currentValue.length() + 1];
        Arrays.fill(backspaces, Keys.BACK_SPACE);
        Arrays.fill(deletes, Keys.DELETE);
        elementWithValue.sendKeys(backspaces);
        elementWithValue.sendKeys(deletes);
    }

    /**
     * Sets the value of an element by sending it keys.
     * <p>
     * This will fully clear the element first before sending any keys. It will
     * also tab out of the element to make sure any form validation is raised.
     *
     * @param elementWithValue The element to set the value for.
     * @param value            The value to set.
     */
    public void setValueOfElement(WebElement elementWithValue, String value) {
        setValueOfElement(elementWithValue, value, Keys.TAB);
    }

    /**
     * Sets the value of an element by sending it keys.
     * <p>
     * This will fully clear the element first before sending any keys. It will
     * also tab out of the element to make sure any form validation is raised.
     *
     * @param elementWithValue The element to set the value for.
     * @param value            The value to set.
     * @param endOfInput       The last key to send.  This is usually something like Keys.TAB or Keys.ENTER.
     *                         You can set this to null to not send anything.
     */
    public void setValueOfElement(WebElement elementWithValue, String value, CharSequence endOfInput) {
        setValueOfElement(elementWithValue, value, new CharSequence[] {endOfInput});
    }

    /**
     * Sets the value of an element by sending it keys.
     * <p>
     * This will fully clear the element first before sending any keys. It will
     * also tab out of the element to make sure any form validation is raised.
     *
     * @param elementWithValue The element to set the value for.
     * @param value            The value to set.
     * @param endOfInput       The last set of keys to send.  This is usually something like Keys.TAB or Keys.ENTER.
     *                         You can set this to null to not send anything.
     */
    public void setValueOfElement(WebElement elementWithValue, String value, CharSequence[] endOfInput) {
        clearValueOfElement(elementWithValue);

        if (value != null) {
            elementWithValue.sendKeys(value);
        }

        if (endOfInput != null && endOfInput.length > 0) {
            elementWithValue.sendKeys(endOfInput);
        }
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
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS / 12));
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
     * Waits for a maximum of forHowLong for a given predicate to be true.
     *
     * @param toBeTrue   The predicate to check for truth.
     * @param forHowLong The duration of how long to wait before throwing an exception.
     */
    public void waitForCondition(Supplier<Boolean> toBeTrue, Duration forHowLong) {
        WebDriverWait wait = new WebDriverWait(driver, forHowLong);
        wait.until((d) -> toBeTrue.get());
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public void waitForElementsToNotAppear(By element) {
        long maxWaitTime = 120L;

        new WebDriverWait(driver, Duration.ofSeconds(maxWaitTime))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(invisibilityOfElementLocated(element));
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public void waitForElementsToNotAppear(By element, long timeoutInMinutes) {
        long webDriverWait = 120L;

        new WebDriverWait(driver, Duration.ofSeconds(webDriverWait * timeoutInMinutes))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(invisibilityOfElementLocated(element));
    }

    /**
     * Waits for the element to be invisible
     *
     * @param element - the element
     * @return true/false
     */
    public void waitForElementsToNotAppear(List<WebElement> element) {
        long webDriverWait = 120L;

        new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(invisibilityOfAllElements(element));
    }

    /**
     * Checks element is displayed
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementAppear(WebElement element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;
        Exception ex;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(visibilityOf(element));

            } catch (Exception e) {
                ex = e;
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }

            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Exception caught: %s", ex.getMessage()));
            }

        }
        return element;
    }

    /**
     * Checks element is displayed
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToAppear(WebElement element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;
        Exception ex;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(visibilityOf(element));

            } catch (Exception e) {
                ex = e;
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }

            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Exception caught: %s", ex.getMessage()));
            }
        }
        return element;
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public WebElement waitForElementToAppear(By element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(visibilityOfElementLocated(element));

            } catch (Exception e) {
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }
        }
        return driver.findElement(element);
    }

    /**
     * Waits for an element to become visible.
     *
     * @param by         The search query to search the context for.
     * @param forHowLong The maximum amount of time to wait.
     * @param search     The parent context to search under.
     * @return The WebElement that was found.
     * @throws TimeoutException If there is never an element that becomes visible.
     */
    public WebElement waitForElementToAppear(By by, Duration forHowLong, SearchContext search) {
        waitForCondition(() -> {
            WebElement element = search.findElements(by).stream().findFirst().orElse(null);
            return element != null;
        }, forHowLong);
        return search.findElement(by);
    }

    /**
     * Checks the elements is displayed by size
     *
     * @param element - the element
     * @return int
     */
    public List<WebElement> waitForElementsToAppear(By element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(visibilityOfAllElementsLocatedBy(element));

            } catch (Exception e) {
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }
        }
        return driver.findElements(element);
    }

    /**
     * Checks element is not displayed by size
     *
     * @param element - the element
     * @return size as int
     */
    public List<WebElement> waitForElementsToAppear(List<WebElement> element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;
        Exception ex;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(visibilityOfAllElements(element));

            } catch (Exception e) {
                ex = e;
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }

            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Exception caught: %s", ex.getMessage()));
            }
        }
        return element;
    }

    /**
     * Checks the specific elements number are displayed
     *
     * @param element - the element
     * @return int
     */
    public List<WebElement> waitForSpecificElementsNumberToAppear(By element, int size) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(numberOfElementsToBe(element, size));

            } catch (Exception e) {
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }
        }
        return driver.findElements(element);
    }

    /**
     * Checks element is displayed
     *
     * @param element - the element
     * @return webelement
     */
    public void waitForEitherElementAppear(By element, By element2) {
        long webDriverWait = 60L;

        new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(or(visibilityOfElementLocated(element),
                visibilityOfElementLocated(element2)
            ));
    }

    /**
     * Wait for element to be clickable
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;
        Exception ex;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(elementToBeClickable(element));

            } catch (Exception e) {
                ex = e;
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }

            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Exception caught: %s", ex.getMessage()));
            }
        }
        return element;
    }

    /**
     * Wait for element to be clickable
     *
     * @param element - the element
     * @return webelement
     */
    public WebElement waitForElementToBeClickable(By element) {
        long webDriverWait = 5L;
        int retries = 0;
        int maxRetries = 12;
        Exception ex;

        while (retries < maxRetries) {
            try {

                return new WebDriverWait(driver, Duration.ofSeconds(webDriverWait))
                    .ignoreAll(ignoredWebDriverExceptions)
                    .until(elementToBeClickable(element));

            } catch (Exception e) {
                ex = e;
                logger.info(String.format("Trying to recover from exception: %s", e.getClass().getName()));
                retries++;
            }

            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Exception caught: %s", ex.getMessage()));
            }
        }
        return driver.findElement(element);
    }

    /**
     * Ignores exceptions, waits for the element to be clickable then clicks it
     *
     * @param element - the locator of the element
     */
    public void waitForElementAndClick(WebElement element) {
        long startTime = System.currentTimeMillis() / 1000;
        long maxWaitTime = 30L;
        long duration = 0;
        Exception ex;

        waitForElementToBeClickable(element);

        while (duration < startTime) {
            try {

                element.click();
                break;

            } catch (Exception e) {
                ex = e;
                duration = (System.currentTimeMillis() / 1000) - startTime;
            }

            if (duration >= maxWaitTime) {
                throw new RuntimeException(String.format("Exception: %s, %s", ex.getClass().getName(), ex.getMessage()));
            }
        }
        this.waitForJavascriptLoadComplete();
    }

    /**
     * Ignores exceptions, waits for the element to be clickable then clicks it
     *
     * @param element - the locator of the element
     */
    public void waitForElementAndClick(By element) {
        long startTime = System.currentTimeMillis() / 1000;
        long maxWaitTime = 30L;
        long duration = 0;
        Exception ex;

        waitForElementToBeClickable(element);

        while (duration < startTime) {
            try {

                driver.findElement(element).click();
                break;

            } catch (Exception e) {
                ex = e;
                duration = (System.currentTimeMillis() / 1000) - startTime;
            }

            if (duration >= maxWaitTime) {
                throw new RuntimeException(String.format("Exception: %s, %s", ex.getClass().getName(), ex.getMessage()));
            }
        }
    }

    /**
     * @param element The element to check is clickable
     * @return True if the element is clickable
     */
    public boolean isElementClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
    // TODO z;
    public void selectDropdownOption(WebElement locator, String dropdownOption) {
        new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS))
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
        new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(not((ExpectedCondition<Boolean>) element -> (locator).isDisplayed()));
    }

    /**
     * Waits for the element to become invisible
     *
     * @param locator - the locator of the element
     * @return
     */
    public void waitForElementNotVisible(WebElement locator, int timeoutInMinutes) {
        new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes))
            .ignoreAll(ignoredWebDriverExceptions)
            .until(ExpectedConditions.invisibilityOf(locator));
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

            textPresent = new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS))
                .withMessage("Expected: " + text.replace("", " ") + "Found: " + waitForElementToAppear(locator).getText())
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

            textPresent = new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS * timeoutInMinutes))
                .withMessage("Not expecting: " + text + "Found: " + waitForElementToAppear(locator).getText())
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

        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInMinutes))
            .withMessage("Expected attribute: " + attribute + "\t" + "Found: " + locator.getAttribute(attribute))
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

        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInMinutes))
            .withMessage("Element not visible using locator: " + locator)
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
        final int timeoutInMinutes = BASIC_WAIT_TIME_IN_SECONDS / 2;

        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInMinutes))
            .withMessage("Expected: " + text + "\t" + "Found: " + locator.getAttribute(attribute))
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
        return new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS / 2))
            .withMessage("Expected option not in dropdown: " + text + "Locator: " + locator)
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
        new WebDriverWait(driver, Duration.ofSeconds(BASIC_WAIT_TIME_IN_SECONDS / 2))
            .withMessage("Expected option not in dropdown: " + option + "Locator: " + locator)
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
     * Get report link element
     *
     * @return WebElement
     */
    public WebElement getReportElement(String reportName) {
        By reportLinkLocator = By.xpath(String.format("//a[contains(text(), '%s')]", reportName));
        waitForElementToAppear(reportLinkLocator);
        return driver.findElement(reportLinkLocator);
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
    //TODO z:
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
     * @param root             - the bottom level of the locator. this is the page the element is located on eg. can be in a modal dialog
     * @param locatorValue     - the locator value
     * @return current page object
     */
    public void typeAheadSelect(WebElement dropdownSelector, String root, String locatorValue) {
        if (!waitForElementToAppear(By.xpath(String.format("//div[@id='%s']//div[@class]", root))).getAttribute("textContent").equals(locatorValue)) {
            waitForElementAndClick(dropdownSelector);
            waitForElementAndClick(By.xpath(String.format("//div[@id='%s']//div[.='%s']//div[@id]", root, locatorValue)));
        }
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param label            - the label preceding the element
     * @param locatorValue     - the locator value
     * @return current page object
     */
    public void optionsTypeAheadSelect(WebElement dropdownSelector, String label, String locatorValue) {
        if (!waitForElementToAppear(By.xpath(String.format("//label[text()='%s']/..//div[@class]", label))).getAttribute("textContent").equals(locatorValue)) {
            waitForElementAndClick(dropdownSelector);
            javaScriptClick(driver.findElement(By.xpath(String.format("//label[text()='%s']/..//div[.='%s']//div[@id]", label, locatorValue))));
        }
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param label            - the label preceding the element
     * @param locatorValue     - the locator value
     * @return current page object
     */
    public void modalTypeAheadSelect(WebElement dropdownSelector, String label, String locatorValue) {
        if (!waitForElementToAppear(By.xpath(String.format("//div[@id='modal-body']//label[text()='%s']/..//div[@class]", label))).getAttribute("textContent").equals(locatorValue)) {
            waitForElementAndClick(dropdownSelector);
            waitForElementAndClick(By.xpath(String.format("//label[text()='%s']/..//div[.='%s']//div[@id]", label, locatorValue)));
        }
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

    /**
     * Gets the label for a value in a given section.
     *
     * @param tag  The HTML tag of the target element.
     * @param text The text of the element to search for.
     * @param root The parent search context element.
     * @return The web element found or null if no such element appears within a reasonable timeframe.
     */
    public WebElement findElementByText(String tag, String text, SearchContext root) {
        try {
            By query = By.xpath(String.format("//%s[.='%s']", tag, text));
            waitForCondition(() -> root.findElements(query).size() > 0, DURATION_FAST);
            return root.findElement(query);
        } catch (TimeoutException | NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Attempts to find a supported loader element.
     *
     * @param search The search root to search for a loader under.
     * @return An element that this utilities object thinks is a loader.  Null otherwise.
     */
    public WebElement findLoader(SearchContext search) {
        final By aprioriLoaderQuery = By.className("loader");
        final WebElement aPrioriLoader = Obligation.optional(() -> waitForElementToAppear(aprioriLoaderQuery, DURATION_INSTANT, search));

        if (aPrioriLoader != null) {
            return aPrioriLoader;
        }

        final By fontAwesomeSpinnerQuery = By.cssSelector(".fa-spinner.fa-spin");
        final WebElement fontAwesomeSpinner = Obligation.optional(() -> waitForElementToAppear(fontAwesomeSpinnerQuery, DURATION_INSTANT, search));
        return fontAwesomeSpinner;
    }

    /**
     * Waits for a page to load, and to complete running all default JavaScript that might be associated
     * with loading that page.
     * before throwing exception
     */
    public void waitForJavascriptLoadComplete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until((ExpectedCondition<Boolean>) wdriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Waits for a WebElement to become visible.
     *
     * @param element - WebElement for which visibility is being waited.
     * @return <b>True</b> if WebElement is visible, <b>false</b> if not visible
     */
    public Boolean waitForWebElement(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Check if the checkbox is selected or not. Important: this is not a wait method, it only shows
     * the current status of the checkbox.
     *
     * @param element - WebElement
     * @return - returns whether checkbox is selected or not
     */
    public boolean isCheckboxSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * right click on element
     *
     * @return void
     */
    public void rightClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.contextClick(element).perform();
    }

    /**
     * Navigate to url, stop page loading and get the redirected current url
     *
     * @param url - url to navigate
     * @return current redirected url
     */
    public String stopPageLoadAndGetCurrentUrl(String url) {
        String currentUrl;
        try {
            driver.manage().timeouts().pageLoadTimeout(DURATION_SLOW);
            driver.navigate().to(url);
            currentUrl = driver.getCurrentUrl();
        } catch (Exception interruptedException) {
            currentUrl = driver.getCurrentUrl();
        }
        return currentUrl;
    }

}
