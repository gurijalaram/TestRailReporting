package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioHoopsImage.json")
public class ScenarioHoopsImage {

    private ScenarioHoopsImage response;
    private String image;
    private String identity;
    private String imageType;

    public String getImage() {
        return image;
    }

    public String getIdentity() {
        return identity;
    }

    public String getImageType() {
        return imageType;
    }
}
