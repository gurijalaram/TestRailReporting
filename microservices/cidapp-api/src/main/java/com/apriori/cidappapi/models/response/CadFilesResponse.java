package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CadFilesResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("response")
public class CadFilesResponse {
    private List<CadFile> cadFiles;
}
