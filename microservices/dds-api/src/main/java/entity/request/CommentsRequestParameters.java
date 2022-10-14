package entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequestParameters {
    private String status;
    private String content;
    private ArrayList<String> mentionedUserEmails;
}
