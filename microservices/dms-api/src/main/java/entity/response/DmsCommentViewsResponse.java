package entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DmsCommentViewsResponseSchema.json")
public class DmsCommentViewsResponse extends Pagination {
    private ArrayList<DmsCommentViewResponse> items;
}
