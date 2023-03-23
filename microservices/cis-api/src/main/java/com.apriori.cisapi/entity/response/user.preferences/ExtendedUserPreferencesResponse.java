package com.apriori.cisapi.entity.response.user.preferences;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ExtendedUserPreferencesResponseSchema.json")
@Data
@JsonRootName("response")
public class ExtendedUserPreferencesResponse extends Pagination {
    private List<ExtendedUserPreferenceParameters> items;
}
