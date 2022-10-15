package pg.lib.cqrs.command;

import lombok.extern.slf4j.Slf4j;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultCommandExecutor implements CommandExecutor {
    private final Map<Class<?>, CommandHandler> commandHandlers;

    public DefaultCommandExecutor(final Collection<CommandHandler> commandHandlers) {
        this.commandHandlers = new HashMap<>();

        if (!commandHandlers.isEmpty()) {
            log.info("------------------ Registering found CommandHandler beans --------------------------\n");
            commandHandlers.forEach(this::addCommandHandler);
            log.info("------------------ Registering CommandHandlers completed  --------------------------");
        }
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
        log.info("CommandHandler: %s%n".formatted(handler.getClass()));
        this.commandHandlers.put(ClassUtils.findInterfaceParameterType(handler.getClass(), CommandHandler.class, 0), handler);
    }
}