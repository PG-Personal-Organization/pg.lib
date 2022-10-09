package pg.lib.cqrs.command;

public interface CommandHandler<Command, CommandResult> {
    CommandResult handle(final Command command);
}
