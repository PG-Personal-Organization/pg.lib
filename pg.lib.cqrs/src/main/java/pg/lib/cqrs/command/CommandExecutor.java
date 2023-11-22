package pg.lib.cqrs.command;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;

/**
 * The interface Command executor.
 */
public interface CommandExecutor {
    /**
     * Execute command result.
     *
     * @param <CommandResult> the type parameter
     * @param <CommandType>   the type parameter
     * @param command         the command
     * @return the command result
     * @throws CommandHandlerNotFoundException the command handler not found exception
     */
    <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command) throws CommandHandlerNotFoundException;
}
