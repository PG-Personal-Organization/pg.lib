package pg.lib.cqrs.command;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;

public interface CommandExecutor {
    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command) throws CommandHandlerNotFoundException;
}
