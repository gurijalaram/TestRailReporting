package com.apriori.cis.api.models.response.scenariodiscussion;

import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectUserDetails;
import com.apriori.shared.util.annotations.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@Schema(location = "UsersResponseSchema.json")
public class ScenarioUsersResponse extends ArrayList<BidPackageProjectUserDetails> {

}
