package ru.unlegit.resthooks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.unlegit.resthooks.util.Pair;

import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestHookData {

    String path;
    Pair<String, String>[] headers;
    Map<String, EndPointData> endPoints;

    public EndPointData getEndPoint(String methodName) {
        return endPoints.get(methodName);
    }
}