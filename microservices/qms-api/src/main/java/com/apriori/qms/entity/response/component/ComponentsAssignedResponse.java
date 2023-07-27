package com.apriori.qms.entity.response.component;

import com.apriori.utils.http.enums.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;


@Data
@Builder
@Schema(location = "ComponentsAssignedResponseSchema.json")
@SuppressWarnings("unused")
public class ComponentsAssignedResponse extends ArrayList<ComponentAssignedParameters> {

}
