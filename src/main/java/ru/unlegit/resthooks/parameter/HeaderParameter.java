package ru.unlegit.resthooks.parameter;

import lombok.AllArgsConstructor;
import ru.unlegit.resthooks.RestRequest;

@AllArgsConstructor
public class HeaderParameter implements RestParameter {

    private final String headerName;

    @Override
    public void resolve(Object argument, RestRequest request) {
        request.addHeader(headerName, (String) argument);
    }
}