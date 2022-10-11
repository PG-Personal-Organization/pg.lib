package pg.lib.cqrs.command;

public interface CommandHandler<CommandType extends Command<CommandResult>, CommandResult> {
    CommandResult handle(final CommandType command);
}
