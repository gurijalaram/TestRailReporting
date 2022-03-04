package com.apriori.acs.entity.response.workorders.getimageinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "workorders/GetImageInfoResponse.json")
public class GetImageInfoResponse {
    private String desktopImageAvailable;
    private String thumbnailAvailable;
    private String partNestingDiagramAvailable;
    private String webImageAvailable;
    private String webImageRequiresRegen;
}
