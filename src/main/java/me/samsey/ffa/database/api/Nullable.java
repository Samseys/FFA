package me.samsey.ffa.database.api;

/**
 * A class to specify the type of a null value.
 */
public class Nullable {

    private final Class<?> nullType;
    private final Object value;

    private Nullable(Class<?> nullType, Object value) {
        this.nullType = nullType;
        this.value = value;
    }

    public static Nullable of(Class<?> nullType, Object value) {
        return new Nullable(nullType, value);
    }

    public Class<?> getNullType() {
        return nullType;
    }

    public Object getValue() {
        return value;
    }
}
