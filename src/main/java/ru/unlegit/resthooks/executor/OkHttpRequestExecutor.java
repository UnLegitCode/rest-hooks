package ru.unlegit.resthooks.executor;

import com.squareup.okhttp.*;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import ru.unlegit.resthooks.RestRequest;
import ru.unlegit.resthooks.json.JsonCodec;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OkHttpRequestExecutor extends AbstractRequestExecutor {

    OkHttpClient client = new OkHttpClient();

    public OkHttpRequestExecutor(@NonNull JsonCodec jsonCodec) {
        super(jsonCodec);
    }

    public void setAuthenticator(Authenticator authenticator) {
        client.setAuthenticator(authenticator);
    }

    @Override
    @SneakyThrows
    public Object executeRequest(RestRequest request) {
        Response response = client.newCall(resolveRequest(request)).execute();

        try (ResponseBody body = response.body()) {
            return resolveResponse(request, body.string(), response.code());
        }
    }

    private static Request resolveRequest(RestRequest request) {
        return (new Request.Builder())
                .url(request.compactPath())
                .method(request.getMethod().name(), request.getBody())
                .headers(resolveHeaders(request))
                .build();
    }

    private static Headers resolveHeaders(RestRequest request) {
        Headers.Builder builder = new Headers.Builder();

        for (Pair<String, String> header : request.getHeaders()) {
            builder.set(header.getKey(), header.getValue());
        }

        return builder.build();
    }
}