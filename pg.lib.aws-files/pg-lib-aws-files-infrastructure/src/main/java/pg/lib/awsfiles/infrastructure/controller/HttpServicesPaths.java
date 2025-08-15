package pg.lib.awsfiles.infrastructure.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServicesPaths {
    public static final String BASE_PATH = "api/v1/files";

    public static final String UPLOAD_PATH = "/rest/upload";

    public static final String UPLOAD_FULL_PATH = BASE_PATH + UPLOAD_PATH;
}
