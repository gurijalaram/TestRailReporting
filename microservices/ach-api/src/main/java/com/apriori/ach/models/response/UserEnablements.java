package com.apriori.ach.models.response;

import lombok.Data;

@Data
public class UserEnablements {
    private Boolean connectAdminEnabled;
    private Boolean highMemEnabled;
    private Boolean previewEnabled;
    private Boolean sandboxEnabled;
    private Boolean userAdminEnabled;
}
