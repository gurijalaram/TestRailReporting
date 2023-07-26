package com.apriori.acs.entity.response.workorders.getimageinfo;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "workorders/ImageInfoResponse.json")
public class ImageInfoResponse {
    private String desktopImageAvailable;
    private String thumbnailAvailable;
    private String partNestingDiagramAvailable;
    private String webImageAvailable;
    private String webImageRequiresRegen;
}
