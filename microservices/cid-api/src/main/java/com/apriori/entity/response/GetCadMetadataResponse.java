package com.apriori.entity.response;

import com.apriori.entity.response.upload.PmiList;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

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
    private PmiList pmiList;
    private String createdAt;
    private String createdBy;
}
