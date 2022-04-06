package com.apriori.apibase.services.nts.objects;

import lombok.Data;

@Data
public class Credentials {
    private String username;
    private String password;
    private String host;
}
