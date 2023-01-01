package pg.lib.filters.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptySpecificationException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Specification predicates are empty!";
    }
}