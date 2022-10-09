package pg.lib.cqrs;

import lombok.NoArgsConstructor;
import pg.lib.cqrs.command.CommandHandler;

@NoArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, String> {

    public String handle(UpdateUserCommand updateUserCommand) {
        return null;
    }
}
