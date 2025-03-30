package pg.lib.awsfiles.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Amazon config.
 */
@Configuration
@ConfigurationProperties(prefix = "aws")
@EntityScan("pg.lib.awsfiles.entity")
@EnableJpaRepositories("pg.lib.awsfiles.repository")
@ComponentScan("pg.lib.awsfiles")
public class AmazonConfig {

    @Getter
    private final String bucketName;
    @Getter
    private final String accessKey;
    @Getter
    private final String secretKey;
    private final AWSCredentials credentials;

    /**
     * Instantiates a new Amazon config.
     *
     * @param bucketName the bucket name
     * @param accessKey  the access key
     * @param secretKey  the secret key
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public AmazonConfig(final @Value("${aws.bucket}") String bucketName,
                        final @Value("${aws.access}") String accessKey,
                        final @Value("${aws.secret}") String secretKey) {
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;

        credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
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
