package pg.lib.remote.cqrs.executors;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.query.Query;

/**
 * The interface Remote cqrs module service executor.
 */
public interface RemoteCqrsModuleServiceExecutor {
    /**
     * Execute query result.
     *
     * @param <QueryResult> the type parameter
     * @param <QueryType>   the type parameter
     * @param query         the query
     * @param module        the module
     * @return the query result
     * @throws RemoteModuleNotFoundException  the remote module not found exception
     * @throws MissMatchResponseTypeException the miss match response type exception
     */
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query, String module) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    /**
     * Execute query result.
     *
     * @param <QueryResult> the type parameter
     * @param <QueryType>   the type parameter
     * @param query         the query
     * @param module        the module
     * @param version       the version
     * @return the query result
     * @throws RemoteModuleNotFoundException  the remote module not found exception
     * @throws MissMatchResponseTypeException the miss match response type exception
     */
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query, String module, int version) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    /**
     * Execute command result.
     *
     * @param <CommandResult> the type parameter
     * @param <CommandType>   the type parameter
     * @param command         the command
     * @param module          the module
     * @return the command result
     * @throws RemoteModuleNotFoundException  the remote module not found exception
     * @throws MissMatchResponseTypeException the miss match response type exception
     */
    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command, String module) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    /**
     * Execute command result.
     *
     * @param <CommandResult> the type parameter
     * @param <CommandType>   the type parameter
     * @param command         the command
     * @param module          the module
     * @param version         the version
     * @return the command result
     * @throws RemoteModuleNotFoundException  the remote module not found exception
     * @throws MissMatchResponseTypeException the miss match response type exception
     */
    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command, String module, int version) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;
}