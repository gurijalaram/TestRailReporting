package com.apriori.cis.models.response.component;

import com.apriori.annotations.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@Schema(location = "AssignComponentsResponseSchema.json")
public class AssignedComponentsResponse extends ArrayList<ComponentParameters> {
}
