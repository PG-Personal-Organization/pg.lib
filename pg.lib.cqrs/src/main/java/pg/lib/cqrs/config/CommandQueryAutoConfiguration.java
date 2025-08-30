package pg.lib.cqrs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * The type Command query autoconfiguration.
 */
@Configuration
@RequiredArgsConstructor
public class CommandQueryAutoConfiguration {

    @Bean
    @SuppressWarnings("rawtypes")
    CommandExecutor commandExecutor(final ObjectProvider<List<CommandHandler>> commandHandlersProvider) {
        return new DefaultCommandExecutor(commandHandlersProvider);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    QueryExecutor queryExecutor(final ObjectProvider<List<QueryHandler>> queryHandlersProvider) {
        return new DefaultQueryExecutor(queryHandlersProvider);
    }

    @Bean
    ServiceExecutor serviceExecutor(final CommandExecutor commandExecutor, final QueryExecutor queryExecutor) {
        return new DefaultServiceExecutor(queryExecutor, commandExecutor);
    }
}
