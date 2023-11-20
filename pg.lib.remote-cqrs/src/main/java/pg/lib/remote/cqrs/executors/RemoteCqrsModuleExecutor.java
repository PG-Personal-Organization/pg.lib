package pg.lib.remote.cqrs.executors;

import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.query.Query;

public interface RemoteCqrsModuleExecutor {
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query) throws RemoteModuleNotFoundException;

    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command) throws RemoteModuleNotFoundException;
}