package pg.lib.awsfiles.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pg.lib.awsfiles.service.api.FileService;
import pg.lib.awsfiles.service.api.InMemoryFileService;

@Configuration
@ComponentScan("pg.lib.awsfiles.infrastructure.controller")
public class InMemoryMockConfiguration {
    @Bean
    @Primary
    public FileService mockFileService() {
        return new InMemoryFileService();
    }
}
