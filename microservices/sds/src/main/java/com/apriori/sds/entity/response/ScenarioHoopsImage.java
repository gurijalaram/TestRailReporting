package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(location = "sds/ScenarioHoopsImage.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioHoopsImage extends JacksonUtil {
    private ScenarioHoopsImage response;
    private String image;
    private String identity;
    private String imageType;
}
