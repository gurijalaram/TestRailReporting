
package com.apriori.cisapi.entity.response.user.preferences;

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

}
