package pg.lib.cqrs.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The type Class utils.
 */
@UtilityClass
public class ClassUtils {

    /**
     * Find interface parameter type class.
     *
     * @param instanceClass   the instance class
     * @param classOfInterest the class of interest
     * @param parameterIndex  the parameter index
     * @return the class
     */
    public Class<?> findInterfaceParameterType(Class<?> instanceClass, final Class<?> classOfInterest, final int parameterIndex) {
        final Map<Type, Type> typeMap = new HashMap<>();
        while (!implementInterface(instanceClass, classOfInterest)) {
            extractTypeArguments(typeMap, instanceClass);
            instanceClass = instanceClass.getSuperclass();
            if (instanceClass == null) {
                throw new IllegalArgumentException();
            }
        }

        final ParameterizedType parameterizedType = findInterface(instanceClass, classOfInterest);
        Type actualType = parameterizedType.getActualTypeArguments()[parameterIndex];
        if (typeMap.containsKey(actualType)) {
            actualType = typeMap.get(actualType);
        }

        if (actualType instanceof Class) {
            return (Class<?>) actualType;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean implementInterface(final Class<?> instanceClass, final Class<?> classOfInterest) {
        return Stream
                .of(instanceClass.getGenericInterfaces())
                .map(ClassUtils::getClass)
                .anyMatch(classOfInterest::equals);
    }

    @SuppressWarnings("rawtypes")
    private Class<?> getClass(final Type type) {
        if (type instanceof Class clazz) {
            return clazz;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return getClass((parameterizedType).getRawType());
        } else if (type instanceof GenericArrayType genericArrayType) {
            final Type componentType = (genericArrayType).getGenericComponentType();
            final Class<?> componentClass = getClass(componentType);

            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static ParameterizedType findInterface(final Class<?> instanceClass, final Class<?> classOfInterest) {
        return Stream
                .of(instanceClass.getGenericInterfaces())
                .filter(typeInterface -> getClass(typeInterface).equals(classOfInterest))
                .findFirst()
                .map(ParameterizedType.class::cast)
                .orElseThrow(RuntimeException::new);
    }

    private static void extractTypeArguments(final Map<Type, Type> typeMap, final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();

        if (!(genericSuperclass instanceof final ParameterizedType parameterizedType)) {
            return;
        }

        final Type[] typeParameter = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        final Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < typeParameter.length; i++) {
            if (typeMap.containsKey(actualTypeArgument[i])) {
                actualTypeArgument[i] = typeMap.get(actualTypeArgument[i]);
            }
            typeMap.put(typeParameter[i], actualTypeArgument[i]);
        }
    }
}
