package entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NexusAsset {
    private String downloadUrl;
    private String path;
    private String id;
    private String repository;
    private String format;
    private NexusCheckSum checksum;
    private String contentType;
    private String lastModified;
}
