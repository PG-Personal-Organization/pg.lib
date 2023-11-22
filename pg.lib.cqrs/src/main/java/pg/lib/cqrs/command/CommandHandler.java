package pg.lib.cqrs.command;

/**
 * The interface Command handler.
 *
 * @param <CommandType>   the type parameter
 * @param <CommandResult> the type parameter
 */
public interface CommandHandler<CommandType extends Command<CommandResult>, CommandResult> {
    /**
     * Handle command result.
     *
     * @param command the command
     * @return the command result
     */
    CommandResult handle(final CommandType command);
}
