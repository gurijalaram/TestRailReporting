package com.apriori.utils.email.response;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "EmailResponseSchema.json")
public class EmailResponse {
    @JsonProperty("@odata.context")
    public String context;
    public ArrayList<EmailMessage> value;
}
