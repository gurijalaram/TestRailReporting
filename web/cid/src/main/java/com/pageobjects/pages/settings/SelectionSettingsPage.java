package com.pageobjects.pages.settings;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ColourEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SelectionSettingsPage extends LoadableComponent<SelectionSettingsPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(SelectionSettingsPage.class);
    private final String colourLocator = "div[class='sp-container sp-light sp-input-disabled sp-palette-buttons-disabled sp-palette-only sp-initial-disabled']";

    @FindBy(css = "div[data-ap-field='selectionColor'] input")
    private WebElement selectedColour;

    @FindBy(css = ".sp-dd")
    private WebElement colourDropdown;

    @FindBy(css = colourLocator + " span[title='#ff0000']")
    private WebElement red;

    @FindBy(css = colourLocator + " span[title='#ffff00']")
    private WebElement yellow;

    @FindBy(css = colourLocator + " span[title='#00ff00']")
    private WebElement lime;

    @FindBy(css = colourLocator + " span[title='#00ffff']")
    private WebElement aqua;

    @FindBy(css = colourLocator + " span[title='#0000ff']")
    private WebElement blue;

    @FindBy(css = colourLocator + " span[title='#ff00ff']")
    private WebElement fuchsia;

    @FindBy(css = colourLocator + " span[title='#ffffff']")
    private WebElement white;

    @FindBy(css = colourLocator + " span[title='#ff0099']")
    private WebElement hollywoodCerise;

    @FindBy(css = colourLocator + " span[title='#ff9900']")
    private WebElement orangePeel;

    @FindBy(css = colourLocator + " span[title='#99ff00']")
    private WebElement springBud;

    @FindBy(css = colourLocator + " span[title='#00ff99']")
    private WebElement mediumSpringGreen;

    @FindBy(css = colourLocator + " span[title='#0099ff']")
    private WebElement dodgerBlue;

    @FindBy(css = colourLocator + " span[title='#9900ff']")
    private WebElement electricPurple;

    @FindBy(css = colourLocator + " span[title='#999999']")
    private WebElement nobel;

    @FindBy(css = colourLocator + " span[title='#990066']")
    private WebElement eggplant;

    @FindBy(css = colourLocator + " span[title='#996600']")
    private WebElement goldenBrown;

    @FindBy(css = colourLocator + " span[title='#669900']")
    private WebElement christi;

    @FindBy(css = colourLocator + " span[title='#009966']")
    private WebElement shamrockGreen;

    @FindBy(css = colourLocator + " span[title='#006699']")
    private WebElement cerulean;

    @FindBy(css = colourLocator + " span[title='#660099']")
    private WebElement indigo;

    @FindBy(css = colourLocator + " span[title='#000000']")
    private WebElement black;

    private WebDriver driver;
    private PageUtils pageUtils;
    private Map<String, WebElement> map = new HashMap<>();

    public SelectionSettingsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Checks the selected colour is is correct
     *
     * @return string
     */
    public String getColour() {
        return selectedColour.getAttribute("value");
    }

    /**
     * Sets the tolerance
     *
     * @param colour - the colour
     * @return
     */
    public SelectionSettingsPage setColour(String colour) {
        pageUtils.waitForElementAndClick(colourDropdown);
        pageUtils.waitForElementToAppear(getMap().get(colour)).click();
        return this;
    }

    private Map<String, WebElement> buildMap() {
        map.put(ColourEnum.RED.getColour(), red);
        map.put(ColourEnum.YELLOW.getColour(), yellow);
        map.put(ColourEnum.LIME.getColour(), lime);
        map.put(ColourEnum.AQUA.getColour(), aqua);
        map.put(ColourEnum.BLUE.getColour(), blue);
        map.put(ColourEnum.FUCHSIA.getColour(), fuchsia);
        map.put(ColourEnum.WHITE.getColour(), white);
        map.put(ColourEnum.HOLLYWOOD_CERISE.getColour(), hollywoodCerise);
        map.put(ColourEnum.ORANGE_PEEL.getColour(), orangePeel);
        map.put(ColourEnum.SPRING_BUD.getColour(), springBud);
        map.put(ColourEnum.MEDIUM_SPRING_GREEN.getColour(), mediumSpringGreen);
        map.put(ColourEnum.DODGER_BLUE.getColour(), dodgerBlue);
        map.put(ColourEnum.ELECTRIC_PURPLE.getColour(), electricPurple);
        map.put(ColourEnum.NOBEL.getColour(), nobel);
        map.put(ColourEnum.EGGPLANT.getColour(), eggplant);
        map.put(ColourEnum.GOLDEN_BROWN.getColour(), goldenBrown);
        map.put(ColourEnum.CHRISTI.getColour(), christi);
        map.put(ColourEnum.SHAMROCK_GREEN.getColour(), shamrockGreen);
        map.put(ColourEnum.CERULEAN.getColour(), cerulean);
        map.put(ColourEnum.INDIGO.getColour(), indigo);
        map.put(ColourEnum.BLACK.getColour(), black);
        return map;
    }

    private Map<String, WebElement> getMap() {
        if (map == null || map.isEmpty()) {
            map = buildMap();
        }
        return map;
    }
}