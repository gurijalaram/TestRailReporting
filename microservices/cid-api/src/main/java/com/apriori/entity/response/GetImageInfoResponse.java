package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "cid/GetImageInfoResponse.json")
public class GetImageInfoResponse {
    private String desktopImageAvailable;
    private String thumbnailAvailable;
    private String partNestingDiagramAvailable;
    private String webImageAvailable;
    private String webImageRequiresRegen;
}
