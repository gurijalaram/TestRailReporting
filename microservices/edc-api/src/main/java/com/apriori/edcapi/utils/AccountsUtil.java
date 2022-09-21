package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.accounts.AccountsItemsResponse;
import com.apriori.edcapi.entity.response.accounts.AccountsResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

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
            RequestEntityUtil.init(EDCAPIEnum.GET_ACCOUNTS, AccountsItemsResponse.class);

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
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.GET_ACCOUNTS_BY_IDENTITY, klass);

        return HTTPRequest.build(requestEntity).get();
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
}
