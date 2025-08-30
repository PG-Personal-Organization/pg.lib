package pg.lib.cqrs.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.ResolvableType;

@UtilityClass
public class ClassUtils {

    /**
     * Resolve the generic parameter type of the implemented interface.
     *
     * @param instanceClass   the concrete class implementing the interface
     * @param classOfInterest the generic interface type (e.g. CommandHandler.class)
     * @param parameterIndex  the index of the generic parameter to resolve
     * @return the resolved class
     */
    public static Class<?> findInterfaceParameterType(
            final Class<?> instanceClass,
            final Class<?> classOfInterest,
            final int parameterIndex) {

        ResolvableType resolvableType = ResolvableType.forClass(instanceClass)
                .as(classOfInterest);

        if (resolvableType == ResolvableType.NONE) {
            throw new IllegalArgumentException(
                    "Class %s does not implement %s"
                            .formatted(instanceClass, classOfInterest));
        }

        Class<?> resolved = resolvableType.getGeneric(parameterIndex).resolve();
        if (resolved == null) {
            throw new IllegalArgumentException(
                    "Could not resolve generic parameter %d of %s for class %s"
                            .formatted(parameterIndex, classOfInterest, instanceClass));
        }

        return resolved;
    }
}
