package com.apriori.nts.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    private String username;
    private String password;
    private String host;
    private String folder;
}
