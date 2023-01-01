package pg.lib.cqrs.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import pg.lib.cqrs.command.CommandExecutor;
import pg.lib.cqrs.command.CommandHandler;
import pg.lib.cqrs.command.DefaultCommandExecutor;
import pg.lib.cqrs.query.DefaultQueryExecutor;
import pg.lib.cqrs.query.QueryExecutor;
import pg.lib.cqrs.query.QueryHandler;
import pg.lib.cqrs.service.DefaultServiceExecutor;
import pg.lib.cqrs.service.ServiceExecutor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommandQueryAutoConfiguration {
    private final Environment env;

    @Bean
    @SuppressWarnings("rawtypes")
    CommandExecutor commandExecutor(final List<CommandHandler> commandHandlers) {
        return new DefaultCommandExecutor(commandHandlers, env);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    QueryExecutor queryExecutor(final List<QueryHandler> queryHandlers) {
        return new DefaultQueryExecutor(queryHandlers, env);
    }

    @Bean
    ServiceExecutor serviceExecutor(final CommandExecutor commandExecutor, final QueryExecutor queryExecutor) {
        return new DefaultServiceExecutor(queryExecutor, commandExecutor);
    }
}