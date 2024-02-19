package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "WorksheetInputRowGetSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class WorkSheetInputRowGetResponse extends Pagination {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private Boolean isRestrictedByAccessControl;
    private List<WorkSheetInputRowResponse> items;
}
