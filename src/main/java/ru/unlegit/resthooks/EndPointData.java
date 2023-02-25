package ru.unlegit.resthooks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.unlegit.resthooks.parameter.RestParameter;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EndPointData {

    String path;
    HttpMethod method;
    RestParameter[] parameters;
    Class<?> responseType;
    boolean wrapResponse;
}