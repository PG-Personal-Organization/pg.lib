package pg.lib.cqrs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pg.lib.cqrs.command.Command;

@AllArgsConstructor(staticName = "of")
@Getter
public class UpdateUserCommand implements Command<String> {
    private String name;
}
