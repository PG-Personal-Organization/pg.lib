package pg.lib.remote.cqrs.executors;

import lombok.ToString;

@ToString
public class RemoteModuleNotFoundException extends RuntimeException {
    public RemoteModuleNotFoundException(final String module) {
        super(String.format("Module: `%s` used in remote communication not found", module));
    }
}
