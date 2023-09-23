package me.samsey.ffa.database;

import java.util.UUID;

public class SQLSingleStat<T> {
    private final UUID uniqueId;
    private final T value;

    public SQLSingleStat(UUID uniqueId, T value) {
        this.uniqueId = uniqueId;
        this.value = value;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public T getValue() {
        return value;
    }
}
