package pg.lib.cqrs.command;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;

import pg.lib.cqrs.exception.CommandHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * The type Default command executor.
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultCommandExecutor implements CommandExecutor {
    private final Map<Class<?>, CommandHandler> commandHandlers;
    private final boolean canLog;

    /**
     * Instantiates a new Default command executor.
     *
     * @param commandHandlers the command handlers
     * @param env             the env
     */
    public DefaultCommandExecutor(final Collection<CommandHandler> commandHandlers, final Environment env) {
        this.commandHandlers = new HashMap<>();

        canLog = Arrays.stream(env.getActiveProfiles()).toList().contains("devlocal");

        if (!commandHandlers.isEmpty()) {
            if (canLog) log.info("------------------ Registering found CommandHandler beans --------------------------\n");
            commandHandlers.forEach(this::addCommandHandler);
            if (canLog) log.info("------------------ Registering CommandHandlers completed  --------------------------");
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
        if (canLog) log.info("CommandHandler: %s%n".formatted(handler.getClass()));
        this.commandHandlers.put(ClassUtils.findInterfaceParameterType(handler.getClass(), CommandHandler.class, 0), handler);
    }
}