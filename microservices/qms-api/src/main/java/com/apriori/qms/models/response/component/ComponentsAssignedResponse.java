package com.apriori.qms.models.response.component;

import com.apriori.annotations.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@Schema(location = "ComponentsAssignedResponseSchema.json")
@SuppressWarnings("unused")
public class ComponentsAssignedResponse extends ArrayList<ComponentAssignedParameters> {

}
