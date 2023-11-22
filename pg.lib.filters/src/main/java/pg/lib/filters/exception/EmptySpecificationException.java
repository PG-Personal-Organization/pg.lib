package pg.lib.filters.exception;

import lombok.NoArgsConstructor;

/**
 * The type Empty specification exception.
 */
@NoArgsConstructor
public class EmptySpecificationException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Specification predicates are empty!";
    }
}