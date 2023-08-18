
package com.apriori.cis.models.request.userpreferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class CurrentUserPreferenceRequest {

    private CurrentUserPreferenceParameters userPreferences;

}