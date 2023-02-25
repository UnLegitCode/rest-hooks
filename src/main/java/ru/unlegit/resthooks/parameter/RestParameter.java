package ru.unlegit.resthooks.parameter;

import ru.unlegit.resthooks.RestRequest;

@FunctionalInterface
public interface RestParameter {

    void resolve(Object argument, RestRequest request);
}