package com.apriori.cidappapi.entity.response.scenarios;

import lombok.Data;

@Data
public class Permissions {
    private String create;
    private String read;
    private String delete;
    private String update;
}
