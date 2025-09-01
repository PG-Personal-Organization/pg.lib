package pg.lib.awsfiles.infrastructure.s3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pg.lib.awsfiles.service.api.AmazonConfig;

@ConfigurationProperties(prefix = "pg.lib.awsfiles")
@Configuration
@Data
public class AmazonConfigImpl implements AmazonConfig {
    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String awsUrl;
}
