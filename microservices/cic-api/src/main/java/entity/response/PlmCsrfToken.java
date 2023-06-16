package entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Schema(location = "PlmCsrfTokenSchema.json")
public class PlmCsrfToken {
    @JsonProperty("@odata.context")
    private String context;
    @JsonProperty("NonceKey")
    private String nonceKey;
    @JsonProperty("NonceValue")
    private String nonceValue;
}