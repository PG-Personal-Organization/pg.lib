package pg.lib.cqrs.command;

import lombok.AllArgsConstructor;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;

import java.util.List;

@AllArgsConstructor
public class DefaultCommandExecutor implements CommandExecutor {

    private final List<CommandHandler<Command<?>, ?>> commandHandlers;

    public <T> T execute(final Command<T> command) throws CommandHandlerNotFoundException {

//        Optional<CommandHandler<Command<T>, T>> commandHandler = commandHandlers.stream()
//                .filter(handler -> handler instanceof CommandHandler<Command<T>, T>)
//                .findFirst();
//
//        if (commandHandler.isEmpty())
//            throw new CommandHandlerNotFoundException(String.format("Command Handler for Command: %s - not found", command.getClass()));
//
//        return commandHandler.get().handle(command);
        return null;
    }
}
