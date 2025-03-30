package pg.lib.cqrs.exception;

/**
 * The type Command handler not found exception.
 */
public class CommandHandlerNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Command handler not found exception.
     *
     * @param message the message
     */
    public CommandHandlerNotFoundException(final String message) {
        super(message);
    }
}
