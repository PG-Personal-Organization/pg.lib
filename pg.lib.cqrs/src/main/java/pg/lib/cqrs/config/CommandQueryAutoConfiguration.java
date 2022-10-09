package pg.lib.cqrs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.command.CommandExecutor;
import pg.lib.cqrs.command.CommandHandler;
import pg.lib.cqrs.command.DefaultCommandExecutor;
import pg.lib.cqrs.query.DefaultQueryExecutor;
import pg.lib.cqrs.query.Query;
import pg.lib.cqrs.query.QueryExecutor;
import pg.lib.cqrs.query.QueryHandler;

import java.util.List;

@Configuration
public class CommandQueryAutoConfiguration {

    @Bean
    CommandExecutor commandExecutor(final List<CommandHandler<Command<?>, ?>> commandHandlers) {
        return new DefaultCommandExecutor(commandHandlers);
    }

    @Bean
    QueryExecutor queryExecutor(final List<QueryHandler<Query<?>, ?>> queryHandlers) {
        return new DefaultQueryExecutor(queryHandlers);
    }

}
