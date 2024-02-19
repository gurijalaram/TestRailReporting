package com.apriori.cis.ui.utils;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CisGuiTestUtil {

    /**
     * get inner text, split attribute details
     *
     * @param webElement - attributeParentElement
     * @return list of attributes
     */
    public static List<String> getAttributeDetails(WebElement attributeParentElement) {
        return Stream.of(attributeParentElement.getAttribute("innerText").split("\\n")).collect(Collectors.toList());
    }
}
