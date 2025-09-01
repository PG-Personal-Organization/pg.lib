package pg.lib.awsfiles.infrastructure.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pg.lib.awsfiles.service.api.AmazonConfig;

/**
 * The type Amazon config.
 */
@ConditionalOnProperty(value = "pg.lib.awsfiles.enabled", havingValue = "true")
@Import({
        SecurityConfig.class,
        AmazonConfigImpl.class
})
@Configuration
@EntityScan("pg.lib.awsfiles.infrastructure.s3.entity")
@EnableJpaRepositories("pg.lib.awsfiles.infrastructure.s3.repository")
@ComponentScan({"pg.lib.awsfiles.infrastructure.s3", "pg.lib.awsfiles.infrastructure.common"})
public class AmazonConfiguration {

    /**
     * S 3 client amazon s 3.
     *
     * @return the amazon s 3
     */
    @Bean
    AmazonS3 s3client(final AmazonConfig amazonConfig) {
        var credentials = new BasicAWSCredentials(amazonConfig.getAccessKey(), amazonConfig.getSecretKey());
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        amazonConfig.getAwsUrl(), Regions.EU_CENTRAL_1.getName()
                ))
                .withPathStyleAccessEnabled(Boolean.TRUE)
                .build();
    }
}
