package pg.lib.cqrs.service;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.query.Query;

public interface ServiceExecutor {
    <CommandResult, CommandType extends Command<CommandResult>> CommandResult executeCommand(CommandType command) throws CommandHandlerNotFoundException;

    <QueryResult, QueryType extends Query<QueryResult>> QueryResult executeQuery(QueryType query) throws QueryHandlerNotFoundException;
}
