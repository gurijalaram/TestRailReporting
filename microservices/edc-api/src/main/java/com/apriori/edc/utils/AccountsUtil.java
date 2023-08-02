package com.apriori.edc.utils;

import com.apriori.FileResourceUtil;
import com.apriori.TestUtil;
import com.apriori.edc.enums.EDCAPIEnum;
import com.apriori.edc.models.response.accounts.AccountsItemsResponse;
import com.apriori.edc.models.response.accounts.AccountsResponse;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;

import org.apache.http.HttpStatus;

import java.util.List;

public class AccountsUtil extends TestUtil {

    /**
     * Get the list of the accounts
     *
     * @return the response object
     */
    public static List<AccountsResponse> getAllAccounts() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.ACCOUNTS, AccountsItemsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<AccountsItemsResponse> getAllAccountsResponse = HTTPRequest.build(requestEntity).get();

        return getAllAccountsResponse.getResponseEntity().getItems();
    }

    /**
     * Get an account by Identity
     *
     * @param identity - the identity
     * @return the response object
     */
    public static ResponseWrapper<AccountsResponse> getAccountByIdentity(String identity) {
        return getAccountByIdentity(identity, AccountsResponse.class, HttpStatus.SC_OK);
    }

    /**
     * Get an account by Identity
     *
     * @param identity - the identity
     * @param klass    - the klass
     * @return response object
     */
    public static ResponseWrapper<AccountsResponse> getAccountByIdentity(String identity, Class klass, Integer expectedResponseCode) {
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.ACCOUNT_BY_IDENTITY, klass)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Create a new account
     *
     * @return the response object
     */
    public ResponseWrapper<AccountsResponse> postCreateNewAccount() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.ACCOUNTS, AccountsResponse.class)
                .body(postBodyInformation())
                .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * This method has a json file to input info for the accounts body
     *
     * @return response object
     */
    private static AccountsResponse postBodyInformation() {
        return JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateAccountData.json").getPath(), AccountsResponse.class);
    }

    /**
     * Delete an account by identity
     *
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<AccountsResponse> deleteAccountByIdentity(String identity) {
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.ACCOUNT_BY_IDENTITY, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Patch update an account
     *
     * @param identity - the identity
     * @return response object
     */
    public ResponseWrapper<AccountsResponse> patchUpdateAccountByIdentity(String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.ACCOUNT_BY_IDENTITY, AccountsResponse.class)
                .inlineVariables(identity)
                .body(postBodyInformation())
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST Activate an account
     *
     * @param identity - the identity
     * @return response object
     */
    public ResponseWrapper<AccountsResponse> postActivateAnAccount(String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.ACTIVATE_ACCOUNT_BY_IDENTITY, AccountsResponse.class)
                .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post refresh the license by identity
     *
     * @param identity - the identity
     * @return response object
     */
    public ResponseWrapper<AccountsResponse> postRefreshLicense(String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.REFRESH_LICENSE_BY_IDENTITY, AccountsResponse.class)
                .inlineVariables(identity)
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET current representation of an active account
     *
     * @return response object
     */
    public ResponseWrapper<AccountsResponse> getActiveAccount() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.CURRENT_ACTIVE_ACCOUNT, AccountsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }
}
