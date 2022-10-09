package pg.lib.cqrs.command;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;

public interface CommandExecutor {
    <T> T execute(Command<T> command) throws CommandHandlerNotFoundException;
}
