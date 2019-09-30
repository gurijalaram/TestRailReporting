package com.apriori.pageobjects.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.awt.Toolkit;

/**
 * @author kpatel
 */
public class MaximizeBrowserOnUnix {

    public static void maximizeOnUnixSystems(WebDriver driver) {
        Point targetPosition = new Point(0, 0);
        driver.manage().window().setPosition(targetPosition);

        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        driver.manage().window().setSize(new Dimension(width, height));
    }

}
