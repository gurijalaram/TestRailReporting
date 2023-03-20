
package com.apriori.cisapi.entity.request.user.preferences;

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
