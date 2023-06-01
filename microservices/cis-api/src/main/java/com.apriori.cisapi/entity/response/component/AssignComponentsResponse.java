package com.apriori.cisapi.entity.response.component;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@Schema(location = "AssignComponentsResponseSchema.json")
public class AssignComponentsResponse extends ArrayList<BidPackageItemParameters> {

}
