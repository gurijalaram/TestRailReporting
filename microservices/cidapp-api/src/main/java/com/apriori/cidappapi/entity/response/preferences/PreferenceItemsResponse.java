package com.apriori.cidappapi.entity.response.preferences;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "PreferencesItemsResponse.json")
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreferenceItemsResponse extends Pagination {
    List<PreferenceResponse> items;
}
