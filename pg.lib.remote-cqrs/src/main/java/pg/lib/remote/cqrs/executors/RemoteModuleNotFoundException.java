package pg.lib.remote.cqrs.executors;

import lombok.ToString;

/**
 * The type Remote module not found exception.
 */
@ToString
public class RemoteModuleNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Remote module not found exception.
     *
     * @param module the module
     */
    public RemoteModuleNotFoundException(final String module) {
        super(String.format("Module: `%s` used in remote communication not found", module));
    }
}
