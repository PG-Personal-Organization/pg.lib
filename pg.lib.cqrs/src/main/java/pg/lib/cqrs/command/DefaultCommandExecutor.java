package pg.lib.cqrs.command;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultCommandExecutor implements CommandExecutor {
    private final Map<Class<?>, CommandHandler> commandHandlers;

    public DefaultCommandExecutor(final Collection<CommandHandler> commandHandlers) {
        this.commandHandlers = new HashMap<>();
        commandHandlers.forEach(this::addCommandHandler);
    }

    @Override
    public <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(CommandType command) throws CommandHandlerNotFoundException {

        final CommandHandler<CommandType, CommandResult> commandHandler = commandHandlers.get(command.getClass());

        if (isNull(commandHandler)) {
            throw new CommandHandlerNotFoundException("Command Handler for Command: %s - not found".formatted(command.getClass()));
        }

        return commandHandler.handle(command);
    }

    private void addCommandHandler(final CommandHandler handler) {
        this.commandHandlers.put(ClassUtils.findInterfaceParameterType(handler.getClass(), CommandHandler.class, 0), handler);
    }
}