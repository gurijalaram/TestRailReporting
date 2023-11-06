package com.apriori.acs.api.models.response.workorders.getimageinfo;

import com.apriori.shared.util.annotations.Schema;

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
