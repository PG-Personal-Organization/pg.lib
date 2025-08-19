package pg.lib.awsfiles.infrastructure.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Amazon config.
 */
@Configuration
@Import({
        SecurityConfig.class
})
@ConditionalOnProperty(value = "pg.lib.awsfiles.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "pg.lib.awsfiles")
@EntityScan("pg.lib.awsfiles.entity")
@EnableJpaRepositories("pg.lib.awsfiles.infrastructure.repository")
@ComponentScan("pg.lib.awsfiles")
@Data
public class AmazonConfig {
    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String awsUrl;

    private final AWSCredentials credentials;

    public AmazonConfig() {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    /**
     * S 3 client amazon s 3.
     *
     * @return the amazon s 3
     */
    @Bean
    AmazonS3 s3client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }
}
