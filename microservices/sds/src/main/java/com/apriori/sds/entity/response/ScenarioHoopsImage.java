package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioHoopsImage.json")
public class ScenarioHoopsImage extends LombokUtil {
    private ScenarioHoopsImage response;
    private String image;
    private String identity;
    private String imageType;
}
