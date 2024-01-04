package ru.unlegit.resthooks.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Pair<L, R> {

    L left;
    R right;

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }
}