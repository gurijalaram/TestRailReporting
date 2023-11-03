package com.apriori.cis.api.models.response.component;

import com.apriori.shared.util.annotations.Schema;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@Schema(location = "AssignComponentsResponseSchema.json")
public class AssignedComponentsResponse extends ArrayList<ComponentParameters> {
}
