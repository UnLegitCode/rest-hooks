package ru.unlegit.resthooks.executor;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import ru.unlegit.resthooks.RestRequest;
import ru.unlegit.resthooks.json.JsonCodec;
import ru.unlegit.resthooks.util.Pair;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OkHttpRequestExecutor extends AbstractRequestExecutor {

    OkHttpClient client;

    public OkHttpRequestExecutor(@NonNull JsonCodec jsonCodec, @Nullable Authenticator authenticator) {
        super(jsonCodec);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (authenticator != null) {
            clientBuilder.authenticator(authenticator);
        }

        client = clientBuilder.build();
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("DataFlowIssue")
    public Object executeRequest(RestRequest request) {
        try (Response response = client.newCall(resolveRequest(request)).execute(); ResponseBody body = response.body()) {
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
            builder.set(header.left(), header.right());
        }

        return builder.build();
    }
}