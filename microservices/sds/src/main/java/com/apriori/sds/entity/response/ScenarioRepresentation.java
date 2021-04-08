package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(location = "sds/ScenarioRepresentation.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioRepresentation extends JacksonUtil {
    private ScenarioRepresentation response;
}
