package main.java.header;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class ExploreHeader extends LoadableComponent<ExploreHeader> {

    private static Logger logger = LoggerFactory.getLogger(ExploreHeader.class);

    private WebDriver driver;
    private PageUtils pageUtils;
    private GenericHeader genericHeader;

    public ExploreHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.genericHeader = new GenericHeader(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Selects new file dropdown
     *
     * @return
     */
    public GenericHeader selectNewFileDropdown() {
        return genericHeader.selectNewFileDropdown();
    }

    /**
     * Selects component button
     *
     * @return
     */
    public GenericHeader selectComponentButton() {
        return genericHeader.selectComponentButton();
    }

    /**
     * Selects scenario button
     *
     * @return
     */
    public GenericHeader selectScenarioButton() {
        return genericHeader.selectScenarioButton();
    }

    /**
     * Selects comparison button
     *
     * @return
     */
    public GenericHeader selectComparisonButton() {
        return genericHeader.selectComparisonButton();
    }

    /**
     * Selects actions button
     *
     * @return
     */
    public GenericHeader selectActionsButton() {
        return genericHeader.selectActionsButton();
    }

    /**
     * Selects edit button
     *
     * @return
     */
    public GenericHeader selectEditButton() {
        return genericHeader.selectEditButton();
    }

    /**
     * Selects lock button
     *
     * @return
     */
    public GenericHeader selectLockButton() {
        return genericHeader.selectLockButton();
    }

    /**
     * Selects assign button
     *
     * @return
     */
    public GenericHeader selectAssignButton() {
        return genericHeader.selectAssignButton();
    }

    /**
     * Selects scenario notes button
     *
     * @return
     */
    public GenericHeader selectScenarioNotesButton() {
        return genericHeader.selectScenarioNotesButton();
    }

    /**
     * Selects publish button
     *
     * @return
     */
    public GenericHeader selectPublishButton() {
        return genericHeader.selectPublishButton();
    }

    /**
     * Get delete button
     * @return
     */
    public boolean getDeleteButton() {
        return genericHeader.getDeleteButton();
    }
}