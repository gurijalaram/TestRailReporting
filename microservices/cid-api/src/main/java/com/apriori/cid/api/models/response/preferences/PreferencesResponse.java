package com.apriori.cid.api.models.response.preferences;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "PreferencesResponse.json")
@AllArgsConstructor
@Builder
@Data
@JsonRootName("response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PreferencesResponse {
    List<Successes> successes;
    List<Failures> failures;
}
