
package com.apriori.cis.models.response.userpreferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class ExtendedUserPreferenceParameters {

    private String customerIdentity;
    private String email;
    private String familyName;
    private String givenName;
    private String identity;
    private String username;
    private String avatarColor;

}
