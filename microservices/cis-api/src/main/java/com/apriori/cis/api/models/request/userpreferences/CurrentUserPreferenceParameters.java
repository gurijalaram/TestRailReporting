
package com.apriori.cis.api.models.request.userpreferences;

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
