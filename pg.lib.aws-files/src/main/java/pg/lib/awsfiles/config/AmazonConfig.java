package pg.lib.awsfiles.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConfigurationProperties(prefix = "aws")
@EntityScan("pg.lib.awsfiles.entity")
@EnableJpaRepositories("pg.lib.awsfiles.repository")
@ComponentScan("pg.lib.awsfiles.service")
public class AmazonConfig {

    public final String bucketName;
    public final String accessKey;
    public final String secretKey;
    private final AWSCredentials credentials;

    public AmazonConfig(@Value("${aws.bucket}") String bucketName,
                        @Value("${aws.access}") String accessKey,
                        @Value("${aws.secret}") String secretKey) {
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;

        credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }

    public String getBucketName() {
        return bucketName;
    }

    @Bean
    AmazonS3 s3client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }
}
