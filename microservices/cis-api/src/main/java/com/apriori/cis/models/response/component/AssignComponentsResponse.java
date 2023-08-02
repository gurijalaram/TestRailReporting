package com.apriori.cis.models.response.component;

import com.apriori.annotations.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@Schema(location = "AssignComponentsResponseSchema.json")
public class AssignComponentsResponse extends ArrayList<BidPackageItemParameters> {

}
