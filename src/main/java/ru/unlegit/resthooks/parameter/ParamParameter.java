package ru.unlegit.resthooks.parameter;

import lombok.AllArgsConstructor;
import ru.unlegit.resthooks.RestRequest;

@AllArgsConstructor
public class ParamParameter implements RestParameter {

    private final String parameterName;

    @Override
    public void resolve(Object argument, RestRequest request) {
        request.addParameter(parameterName, argument.toString());
    }
}