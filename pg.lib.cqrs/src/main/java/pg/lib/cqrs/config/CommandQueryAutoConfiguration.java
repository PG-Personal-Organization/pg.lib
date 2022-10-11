package pg.lib.cqrs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pg.lib.cqrs.command.CommandExecutor;
import pg.lib.cqrs.command.CommandHandler;
import pg.lib.cqrs.command.DefaultCommandExecutor;
import pg.lib.cqrs.query.DefaultQueryExecutor;
import pg.lib.cqrs.query.QueryExecutor;
import pg.lib.cqrs.query.QueryHandler;

import java.util.List;

@Configuration
public class CommandQueryAutoConfiguration {

    @Bean
    @SuppressWarnings("rawtypes")
    CommandExecutor commandExecutor(final List<CommandHandler> commandHandlers) {
        return new DefaultCommandExecutor(commandHandlers);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    QueryExecutor queryExecutor(final List<QueryHandler> queryHandlers) {
        return new DefaultQueryExecutor(queryHandlers);
    }

}
