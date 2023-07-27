package com.apriori.common;

import com.apriori.PageUtils;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.components.TableHeaderComponent;
import com.apriori.utils.Obligation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class UsersTableController extends LoadableComponent<UsersTableController> {

    @FindBy(css = ".apriori-source-list-layout-table")
    private WebElement usersTableRoot;
    private final SourceListComponent usersTable;

    private final WebDriver driver;
    private PageUtils pageUtils;

    public UsersTableController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        usersTable = new SourceListComponent(driver, usersTableRoot);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(usersTableRoot);
    }

    /**
     * Gets the users list.
     *
     * @return The users list.
     */
    public SourceListComponent getUsersTable() {
        pageUtils.waitForCondition(usersTable::isStable, pageUtils.DURATION_LOADING);
        return usersTable;
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id id of column
     * @param soft soft assertions
     * @return This object
     */
    public <T> T validateUsersTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft, Class<T> klass) {
        SourceListComponent list = getUsersTable();
        TableComponent table = Obligation.mandatory(list::getTable, "The access controls table is missing");

        TableHeaderComponent header = table.getHeader(id);
        soft.assertThat(header)
                .overridingErrorMessage(String.format("The '%s' column is missing.", expectedName))
                .isNotNull();

        if (header != null) {
            String name = header.getName();
            soft.assertThat(name)
                    .overridingErrorMessage(String.format("The '%s' column is incorrectly named '%s'", expectedName, name))
                    .isEqualTo(expectedName);
            soft.assertThat(header.canSort())
                    .overridingErrorMessage(String.format("The '%s' column is not sortable.", name))
                    .isTrue();
        }
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Validates that table is pageable and refreshable
     *
     * @param soft soft assertions
     * @return This object
     */
    public <T> T validateUsersTableArePageableAndRefreshable(SoftAssertions soft, Class<T> klass) {
        SourceListComponent list = getUsersTable();
        soft.assertThat(list.getPaginator())
                .overridingErrorMessage("The customer staff table has no pagination.")
                .isNotNull();
        soft.assertThat(list.canRefresh())
                .overridingErrorMessage("The customer staff table is missing the refresh button.")
                .isTrue();

        return PageFactory.initElements(driver, klass);
    }
}