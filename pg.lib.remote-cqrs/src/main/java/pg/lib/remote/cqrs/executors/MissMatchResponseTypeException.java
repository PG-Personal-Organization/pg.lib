package pg.lib.remote.cqrs.executors;

import lombok.ToString;

/**
 * The type Miss match response type exception.
 */
@ToString
public class MissMatchResponseTypeException extends RuntimeException {
    /**
     * Instantiates a new Miss match response type exception.
     *
     * @param expectedClazz the expected clazz
     * @param responseClazz the response clazz
     */
    public MissMatchResponseTypeException(final Class<?> expectedClazz, final Class<?> responseClazz) {
        super(String.format("Expected response object of type: %s, available type: %s",
                expectedClazz.toString(), responseClazz.toString()));
    }
}
