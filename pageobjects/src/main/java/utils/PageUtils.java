package main.java.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class PageUtils {

    private WebDriver driver;
    public static final int BASIC_WAIT_TIME_IN_SECONDS = 60;

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
            if (alert.getText().equalsIgnoreCase(alertText)) {
                return true;
            } else {
                return false;
            }
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    /**
     * 
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
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if the sub element is displayed or not. Important: this is not a wait method, it only
     * shows the current status of the element.
     * 
     * @param parent - parent element
     * @param by - by
     * @return boolean whether sub-eleent id displayed or not
     */
    public boolean isSubElementDisplayed(WebElement parent, By by) {
        try {
            return parent.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
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
     * @param targetElement - web element
     */
    public void clearInput(WebElement targetElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = ''", targetElement);
    }

    public Dimension getWindowDimension() {
        return driver.manage().window().getSize();
    }

    /*
     * @param scrollDown - true scrolls down and the element is visible on top of the page - false
     * scroll to the top of the page
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

        return (boolean)((JavascriptExecutor)driver).executeScript(
            "var elm = arguments[0];" +
                "var doc = elm.ownerDocument || document;" +
                "var rect = elm.getBoundingClientRect();" +
                "return elm === doc.elementFromPoint(rect.left + (rect.width / 2), rect.top + (rect.height / 2));",
                element);
    }

    /**
     * Waits for a given time
     *
     * @param milis - time in milliseconds
     */
    public void waitFor(Integer milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e1) {
            logger.info("InterruptedException");
        }
    }

    public WebElement waitForElementToAppear(By locator) {
        return waitForElementToAppear(locator, "Element did not appear");
    }

    public WebElement waitForElementToAppear(By locator, String message) {
        return waitForAppear(ExpectedConditions.visibilityOfElementLocated(locator), message);
    }

    public WebElement waitForElementToAppear(WebElement element) {
        return waitForAppear(ExpectedConditions.visibilityOf(element), "Element did not appear");
    }

    public WebElement waitForElementToAppear(WebElement element, String message) {
        return waitForAppear(ExpectedConditions.visibilityOf(element), message);
    }

    public List<WebElement> waitForElementsToAppear(List<WebElement> elements) {
        return waitForAppear(ExpectedConditions.visibilityOfAllElements(elements), "Elements did not appear");
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

    public WebElement waitForChildElementToAppear(WebElement parentElement, By childLocator) {
        int count = 0;
        while (count < 20) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, BASIC_WAIT_TIME_IN_SECONDS / 20);
                return wait.until(ExpectedConditions.visibilityOf(parentElement.findElement(childLocator)));
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
                return wait.until(ExpectedConditions.visibilityOf(parentElement.findElement(childLocator)));
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
            } catch (TimeoutException e) {
                count = count + 1;
            } catch (NoSuchElementException e) {
                count = count + 1;
            }
        }
        throw new AssertionError("Element did not appear: " + childLocator);
    }
}