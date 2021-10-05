package com.apriori.edcapi.tests.util;

import com.apriori.apibase.services.response.objects.BillOfMaterial;
import com.apriori.apibase.services.response.objects.MaterialLineItem;
import com.apriori.apibase.services.response.objects.MaterialPart;
import com.apriori.utils.users.UserCredentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataEDC {

    private String identity;
    private String accountId;

    private String username;
    private String password;
    private String token;

    private List<String> workingIdentities = new ArrayList<>();
    private List<BillOfMaterial> billOfMaterials;
    private BillOfMaterial billOfMaterial;
    private Map<String, String> authorizationHeaders;
    private MaterialLineItem lineItem;
    private MaterialPart materialPart;

    public UserDataEDC() {
    }

    public UserDataEDC(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public UserDataEDC(final UserCredentials userCredentials) {
        this.username = userCredentials.getUsername();
        this.password = userCredentials.getPassword();
    }

    public List<BillOfMaterial> getBillOfMaterials() {
        return billOfMaterials;
    }


    public UserCredentials getUserCredentials() {
        return UserCredentials.init(this.getUsername(), this.getPassword());
    }

    public UserDataEDC setBillOfMaterials(List<BillOfMaterial> billOfMaterials) {
        this.billOfMaterials = billOfMaterials;
        return this;
    }

    public BillOfMaterial getBillOfMaterial() {
        return billOfMaterial;
    }

    public UserDataEDC setBillOfMaterial(BillOfMaterial billOfMaterial) {
        this.billOfMaterial = billOfMaterial;
        return this;
    }

    public Map<String, String> getAuthorizationHeaders() {
        return authorizationHeaders;
    }

    public UserDataEDC setAuthorizationHeaders(Map<String, String> authorizationHeaders) {
        this.authorizationHeaders = authorizationHeaders;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserDataEDC setTokenAndInitAuthorizationHeaders(String token) {
        this.token = token;

        this.authorizationHeaders = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
            }};

        return this;
    }

    public MaterialLineItem getLineItem() {
        return lineItem;
    }

    public UserDataEDC setLineItem(MaterialLineItem lineItem) {
        this.lineItem = lineItem;
        return this;
    }

    public MaterialPart getMaterialPart() {
        return materialPart;
    }

    public UserDataEDC setMaterialPart(MaterialPart materialPart) {
        this.materialPart = materialPart;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDataEDC setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDataEDC setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public UserDataEDC setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public UserDataEDC setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public UserDataEDC setToken(String token) {
        this.token = token;
        return this;
    }

    public List<String> getWorkingIdentities() {
        return workingIdentities;
    }

    public UserDataEDC setWorkingIdentities(List<String> workingIdentities) {
        this.workingIdentities = workingIdentities;
        return this;
    }

    public UserDataEDC addWorkingIdentity(String identityToClear) {
        this.workingIdentities.add(identityToClear);
        return this;
    }
}
