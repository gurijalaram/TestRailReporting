package main.java.api.edc.util;

import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.MaterialLineItem;
import main.java.http.builder.common.response.common.MaterialPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataEDC {

    private String identity;
    private String accountId;

    private String username;
    private String password;
    private String token;

    private List<BillOfMaterial> billOfMaterials;
    private BillOfMaterial billOfMaterial;
    private Map<String, String> authorizationHeaders;
    private MaterialLineItem lineItem;
    private MaterialPart materialPart;

    public UserDataEDC() {
    }

    public UserDataEDC(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public List<BillOfMaterial> getBillOfMaterials() {
        return billOfMaterials;
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
                put("ap-cloud-context", "EDC");
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
}
