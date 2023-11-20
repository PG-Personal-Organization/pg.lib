package pg.lib.remote.cqrs.executors;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.query.Query;

public interface RemoteCqrsModuleServiceExecutor {
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query, String module) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query, String module, int version) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command, String module) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;

    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command, String module, int version) throws RemoteModuleNotFoundException, MissMatchResponseTypeException;
}