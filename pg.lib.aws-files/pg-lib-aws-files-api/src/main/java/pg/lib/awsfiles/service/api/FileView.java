package pg.lib.awsfiles.service.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class FileView {
    private final UUID fileId;
    private final String fileName;
}
