package pg.lib.cqrs.command;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * Default implementation of CommandExecutor that can eagerly register known CommandHandlers
 * and lazily resolve missing ones from the Spring context on first use.
 */
@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultCommandExecutor implements CommandExecutor {
    private final Map<Class<?>, CommandHandler> commandHandlers = new HashMap<>();
    private final ObjectProvider<List<CommandHandler>> commandHandlersProvider;

    @SuppressWarnings("checkstyle:HiddenField")
    public DefaultCommandExecutor(final ObjectProvider<List<CommandHandler>> commandHandlersProvider) {
        this.commandHandlersProvider = commandHandlersProvider;
        var initialHandlers = commandHandlersProvider.getIfAvailable(List::of);
        if (!initialHandlers.isEmpty()) {
            log.debug("Registering {} CommandHandler beans eagerly", initialHandlers.size());
            initialHandlers.forEach(this::addCommandHandler);
            log.debug("Initial CommandHandlers registered");
        }
    }

    @Override
    public <CommandResult, CommandType extends Command<CommandResult>>
    CommandResult execute(final CommandType command) {
        Class<?> commandClass = command.getClass();
        CommandHandler<CommandType, CommandResult> commandHandler = commandHandlers.get(commandClass);

        if (isNull(commandHandler)) {
            log.info("CommandHandler for {} not found in cache, attempting to resolve from Spring context", commandClass);

            commandHandler = resolveHandler(commandClass);

            if (isNull(commandHandler)) {
                throw new CommandHandlerNotFoundException(
                        "CommandHandler for command type %s not found".formatted(commandClass.getName())
                );
            }

            // cache for next use
            commandHandlers.put(commandClass, commandHandler);
            log.info("Resolved and cached CommandHandler {} for {}", commandHandler.getClass(), commandClass);
        }

        return commandHandler.handle(command);
    }

    private void addCommandHandler(final CommandHandler<?, ?> handler) {
        Class<?> commandType = ClassUtils.findInterfaceParameterType(handler.getClass(), CommandHandler.class, 0);
        log.debug("Registering handler {} for command type {}", handler.getClass().getSimpleName(), commandType.getSimpleName());
        commandHandlers.put(commandType, handler);
    }

    @SuppressWarnings("unchecked")
    private <CommandResult, CommandType extends Command<CommandResult>> CommandHandler<CommandType, CommandResult> resolveHandler(final Class<?> commandClass) {
        return commandHandlersProvider.getIfAvailable(List::of).stream()
                .filter(handler ->
                        ClassUtils.findInterfaceParameterType(handler.getClass(), CommandHandler.class, 0)
                                .equals(commandClass))
                .findFirst()
                .orElse(null);
    }
}