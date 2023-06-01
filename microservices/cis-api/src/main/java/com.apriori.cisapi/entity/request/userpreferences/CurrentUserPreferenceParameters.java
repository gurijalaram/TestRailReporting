
package com.apriori.cisapi.entity.request.userpreferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class CurrentUserPreferenceParameters {

    private String avatarColor;

}
