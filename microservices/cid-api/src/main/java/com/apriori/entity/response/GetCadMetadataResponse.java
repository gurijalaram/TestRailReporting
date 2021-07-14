package com.apriori.entity.response;

import com.apriori.entity.response.upload.ManifestItem;
import com.apriori.entity.response.upload.PmiItem;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "cid/GetCadMetadataResponse.json")
public class GetCadMetadataResponse {
    private String fileMetadataIdentity;
    private String cadType;
    private String keepFreeBodies;
    private String freeBodiesPreserveCad;
    private String freeBodiesIgnoreMissingComponents;
    private String lengthUnit;
    private String vendor;
    private List<PmiItem> pmi;
    private List<ManifestItem> manifest;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}
