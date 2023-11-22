package pg.lib.cqrs.service;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.query.Query;

/**
 * The interface Service executor.
 */
public interface ServiceExecutor {
    /**
     * Execute command command result.
     *
     * @param <CommandResult> the type parameter
     * @param <CommandType>   the type parameter
     * @param command         the command
     * @return the command result
     * @throws CommandHandlerNotFoundException the command handler not found exception
     */
    <CommandResult, CommandType extends Command<CommandResult>> CommandResult executeCommand(CommandType command) throws CommandHandlerNotFoundException;

    /**
     * Execute query query result.
     *
     * @param <QueryResult> the type parameter
     * @param <QueryType>   the type parameter
     * @param query         the query
     * @return the query result
     * @throws QueryHandlerNotFoundException the query handler not found exception
     */
    <QueryResult, QueryType extends Query<QueryResult>> QueryResult executeQuery(QueryType query) throws QueryHandlerNotFoundException;
}
