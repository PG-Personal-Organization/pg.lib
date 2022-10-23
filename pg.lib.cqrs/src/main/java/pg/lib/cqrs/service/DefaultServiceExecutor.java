package pg.lib.cqrs.service;

import lombok.AllArgsConstructor;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.command.CommandExecutor;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.query.Query;
import pg.lib.cqrs.query.QueryExecutor;

@AllArgsConstructor
public class DefaultServiceExecutor implements ServiceExecutor {
    private final QueryExecutor queryExecutor;
    private final CommandExecutor commandExecutor;

    @Override
    public <CommandResult, CommandType extends Command<CommandResult>> CommandResult executeCommand(final CommandType command) throws CommandHandlerNotFoundException {
        return commandExecutor.execute(command);
    }

    @Override
    public <QueryResult, QueryType extends Query<QueryResult>> QueryResult executeQuery(final QueryType query) throws QueryHandlerNotFoundException {
        return queryExecutor.execute(query);
    }
}
