package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.accounts.AccountsItemsResponse;
import com.apriori.edcapi.entity.response.accounts.AccountsResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

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
            RequestEntityUtil.init(EDCAPIEnum.ALL_ACCOUNTS, AccountsItemsResponse.class);

        ResponseWrapper<AccountsItemsResponse> getAllAccountsResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllAccountsResponse.getStatusCode());

        return getAllAccountsResponse.getResponseEntity().getItems();
    }

    /**
     * Get an account by Identity
     *
     * @param identity - the identity
     * @return the response object
     */
    public static ResponseWrapper<AccountsResponse> getAccountByIdentity(String identity) {
        return getAccountByIdentity(identity, AccountsResponse.class);
    }

    /**
     * Get an account by Identity
     *
     * @param identity - the identity
     * @param klass    - the klass
     * @return response object
     */
    public static ResponseWrapper<AccountsResponse> getAccountByIdentity(String identity, Class klass) {
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.ACCOUNTS_BY_IDENTITY, klass);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Create a new account
     *
     * @return the response object
     */
    public ResponseWrapper<AccountsResponse> postCreateNewAccount() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.ADD_NEW_ACCOUNT, AccountsResponse.class)
                .body(postBodyInformation());

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
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.DELETE_ACCOUNTS_BY_IDENTITY, null);

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
            RequestEntityUtil.init(EDCAPIEnum.UPDATE_ACCOUNT_BY_IDENTITY, AccountsResponse.class)
                .inlineVariables(identity)
                .body(postBodyInformation());

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

                .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET current representation of an active account
     *
     * @return response object
     */
    public ResponseWrapper<AccountsResponse> getActiveAccount() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.CURRENT_ACTIVE_ACCOUNT, AccountsResponse.class);

        return HTTPRequest.build(requestEntity).get();
    }
}
