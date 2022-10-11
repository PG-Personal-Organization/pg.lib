package pg.lib.cqrs;

import lombok.NoArgsConstructor;
import pg.lib.cqrs.command.CommandHandler;

@NoArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, String> {

    public String handle(final UpdateUserCommand updateUserCommand) {
        return updateUserCommand.getName().toUpperCase();
    }
}
