package pg.lib.remote.cqrs.executors;

import lombok.ToString;

@ToString
public class MissMatchResponseTypeException extends RuntimeException {
    public MissMatchResponseTypeException(final Class<?> expectedClazz, final Class<?> responseClazz) {
        super(String.format("Expected response object of type: %s, available type: %s",
                expectedClazz.toString(), responseClazz.toString()));
    }
}
