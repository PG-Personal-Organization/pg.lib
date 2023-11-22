package pg.lib.cqrs;

import lombok.NoArgsConstructor;
import pg.lib.cqrs.command.CommandHandler;

/**
 * The type Update user command handler.
 */
@NoArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, String> {

    public String handle(final UpdateUserCommand updateUserCommand) {
        return updateUserCommand.getName().toUpperCase();
    }
}
