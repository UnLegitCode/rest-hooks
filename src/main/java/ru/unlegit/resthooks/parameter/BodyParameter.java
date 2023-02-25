package ru.unlegit.resthooks.parameter;

import lombok.AllArgsConstructor;
import ru.unlegit.resthooks.RestRequest;
import ru.unlegit.resthooks.json.JsonCodec;

@AllArgsConstructor
public class BodyParameter implements RestParameter {

    private final JsonCodec serializer;

    @Override
    public void resolve(Object argument, RestRequest request) {
        request.setBody(serializer.encode(argument));
    }
}