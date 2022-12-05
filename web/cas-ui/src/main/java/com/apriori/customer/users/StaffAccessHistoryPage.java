package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.utils.Obligation;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.components.TableHeaderComponent;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public final class StaffAccessHistoryPage extends UsersPage {

    @FindBy(css = ".search-field-input")
    private WebElement searchField;

    private UsersTableController usersTableController;

    public StaffAccessHistoryPage(WebDriver driver) {
        super(driver);
        usersTableController = new UsersTableController(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(searchField);
    }

    /**
     * Gets the users list.
     *
     * @return The users list.
     */
    public SourceListComponent getUsersList() {
        return usersTableController.getUsersTable();
    }

    /**
     * Validates that table is pageable and refreshable
     *
     * @param soft soft assertions
     * @return This object
     */
    public StaffAccessHistoryPage validateStaffHistoryTableArePageableRefreshable(SoftAssertions soft) {
        return usersTableController.validateUsersTableArePageableAndRefreshable(soft, StaffAccessHistoryPage.class);
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id           id of column
     * @param soft         soft assertions
     * @return This object
     */
    public StaffAccessHistoryPage validateHistoryTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        SourceListComponent list = getUsersList();
        TableComponent table = Obligation.mandatory(list::getTable, "The customers table is missing");

        TableHeaderComponent header = table.getHeader(id);
        soft.assertThat(header)
            .overridingErrorMessage(String.format("The '%s' column is missing.", expectedName))
            .isNotNull();

        if (header != null) {
            String name = header.getName();
            soft.assertThat(name)
                .overridingErrorMessage(String.format("The '%s' column is incorrectly named '%s'", expectedName, name))
                .isEqualTo(expectedName);
        }
        return this;
    }
}