package entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlmPart {
    private String id;
    private String typeId;
}