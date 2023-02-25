package ru.unlegit.resthooks;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import javafx.util.Pair;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestRequest {

    private static final MediaType CONTENT_TYPE = MediaType.parse(
            "application/json"
    );
    private static final RequestBody EMPTY_BODY = RequestBody.create(
            CONTENT_TYPE,
            new byte[0]
    );

    final HttpMethod method;
    @NonNull
    String path;
    final List<Pair<String, String>> headers = new ArrayList<>();
    final List<Pair<String, String>> parameters = new LinkedList<>();
    RequestBody body;
    @Setter
    Class<?> responseType;
    @Setter
    boolean wrapResponse;

    public void appendPath(String path) {
        if (!path.isEmpty()) {
            this.path += "/" + path;
        }
    }

    public void addHeaders(Pair<String, String>[] headers) {
        this.headers.addAll(Arrays.asList(headers));
    }

    public void addHeader(String name, String value) {
        headers.add(new Pair<>(name, value));
    }

    public void addParameter(String name, String value) {
        parameters.add(new Pair<>(name, value));
    }

    public void setBody(String json) {
        body = RequestBody.create(CONTENT_TYPE, json);
    }

    public String compactPath() {
        String params = parameters.stream()
                .map(pair -> pair.getKey() + "=" + pair.getValue())
                .reduce((s, s2) -> s + "&" + s2)
                .orElse("");

        return path + (params.isEmpty() ? "" : "?" + params);
    }

    public RequestBody getBody() {
        if (body == null &&
                (method == HttpMethod.POST ||
                 method == HttpMethod.PUT ||
                 method == HttpMethod.PATCH ||
                 method == HttpMethod.OPTIONS ||
                 method == HttpMethod.DELETE
                )) {
            return EMPTY_BODY;
        }

        return body;
    }
}