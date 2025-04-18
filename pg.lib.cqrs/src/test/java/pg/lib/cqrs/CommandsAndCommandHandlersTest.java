package pg.lib.cqrs;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.command.CommandExecutor;
import pg.lib.cqrs.command.DefaultCommandExecutor;
import pg.lib.cqrs.exception.CommandHandlerNotFoundException;

import java.util.Collections;
import java.util.List;

/**
 * The type Commands and command handlers test.
 */
@ExtendWith(MockitoExtension.class)
class CommandsAndCommandHandlersTest {

    private UpdateUserCommand updateUserCommand;
    private CommandExecutor commandExecutor;
    private NewCommand newCommand;
    @Mock
    private Environment env;

    /**
     * Init before each.
     */
    @BeforeEach
    void initBeforeEach() {
        updateUserCommand = UpdateUserCommand.of("NEwName");
        newCommand = new NewCommand();

        final var userCommandHandler = new UpdateUserCommandHandler();

        Mockito.when(env.getActiveProfiles()).thenReturn(List.of("devlocal").toArray(String[]::new));

        commandExecutor = new DefaultCommandExecutor(Collections.singleton(userCommandHandler), env);
    }

    /**
     * Should get proper command handler for command.
     */
    @Test
    void shouldGetProperCommandHandlerForCommand() {
        var commandResult = commandExecutor.execute(updateUserCommand);

        Assertions.assertEquals("NEWNAME", commandResult);
    }

    /**
     * Should throw command handler not found exception.
     */
    @Test
    void shouldThrowCommandHandlerNotFoundException() {
        Assertions.assertThrows(CommandHandlerNotFoundException.class, () -> commandExecutor.execute(newCommand));
    }

    @NoArgsConstructor
    private static class NewCommand implements Command<Void> {
    }
}
