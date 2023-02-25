package ru.unlegit.resthooks.executor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import ru.unlegit.resthooks.HttpStatus;
import ru.unlegit.resthooks.RestHookProxyFactory;
import ru.unlegit.resthooks.RestRequest;
import ru.unlegit.resthooks.RestResponse;
import ru.unlegit.resthooks.json.JsonCodec;

@AllArgsConstructor
public abstract class AbstractRequestExecutor implements RequestExecutor {

    @NonNull
    protected final JsonCodec jsonCodec;

    @Override
    public final <T> T implementHook(@NonNull Class<T> requesterType) {
        return RestHookProxyFactory.createRequesterProxy(requesterType, this, jsonCodec);
    }

    protected Object resolveResponse(RestRequest request, String body, int status) {
        if (body.length() == 0 || request.getResponseType() == void.class) {
            if (request.isWrapResponse()) {
                return new RestResponse<>(HttpStatus.getStatus(status), null);
            } else {
                return null;
            }
        }

        Object responseBody = jsonCodec.decode(body, request.getResponseType());

        if (request.isWrapResponse()) {
            return new RestResponse<>(HttpStatus.getStatus(status), responseBody);
        } else {
            return responseBody;
        }
    }
}