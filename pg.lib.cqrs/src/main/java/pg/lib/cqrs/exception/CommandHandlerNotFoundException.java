package pg.lib.cqrs.exception;

public class CommandHandlerNotFoundException extends RuntimeException {
    public CommandHandlerNotFoundException(String message) {
        super(message);
    }
}
