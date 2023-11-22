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

/**
 * The type Command query auto configuration.
 */
@Configuration
@RequiredArgsConstructor
public class CommandQueryAutoConfiguration {
    private final Environment env;

    /**
     * Command executor command executor.
     *
     * @param commandHandlers the command handlers
     * @return the command executor
     */
    @Bean
    @SuppressWarnings("rawtypes")
    CommandExecutor commandExecutor(final List<CommandHandler> commandHandlers) {
        return new DefaultCommandExecutor(commandHandlers, env);
    }

    /**
     * Query executor query executor.
     *
     * @param queryHandlers the query handlers
     * @return the query executor
     */
    @Bean
    @SuppressWarnings("rawtypes")
    QueryExecutor queryExecutor(final List<QueryHandler> queryHandlers) {
        return new DefaultQueryExecutor(queryHandlers, env);
    }

    /**
     * Service executor service executor.
     *
     * @param commandExecutor the command executor
     * @param queryExecutor   the query executor
     * @return the service executor
     */
    @Bean
    ServiceExecutor serviceExecutor(final CommandExecutor commandExecutor, final QueryExecutor queryExecutor) {
        return new DefaultServiceExecutor(queryExecutor, commandExecutor);
    }
}