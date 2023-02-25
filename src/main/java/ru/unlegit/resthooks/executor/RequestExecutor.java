package ru.unlegit.resthooks.executor;

import lombok.NonNull;
import ru.unlegit.resthooks.RestRequest;

public interface RequestExecutor {

    Object executeRequest(RestRequest request);

    <T> T implementHook(@NonNull Class<T> requesterType);
}