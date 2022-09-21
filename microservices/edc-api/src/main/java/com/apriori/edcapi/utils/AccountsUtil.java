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
        RequestEntity requestEntity = BillOfMaterialsUtil.genericRequest(identity, EDCAPIEnum.GET_ACCOUNTS_BY_IDENTITY, AccountsResponse.class);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Create a new account
     *
     * @return the response object
     */
    public ResponseWrapper<AccountsResponse> postCreateNewAccount() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_ACCOUNTS, AccountsResponse.class)
                .body(postBodyInformation());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * This method has a json file to input info for the accounts body
     *
     * @return response object
     */
    private static AccountsResponse postBodyInformation() {
        String filename = "CreateAccountData.json";
        return (AccountsResponse) BodyInformationUtil.postBodyInformation(filename, AccountsResponse.class);
    }
}
