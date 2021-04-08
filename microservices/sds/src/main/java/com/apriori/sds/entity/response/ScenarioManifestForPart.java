package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioManifestForPart.json")
@Data
public class ScenarioManifestForPart extends JacksonUtil {
    private String path;
    private String error;
    private String message;
    private String timestamp;
    private String status;
}
