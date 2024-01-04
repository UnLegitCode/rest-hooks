package ru.unlegit.resthooks;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.unlegit.resthooks.annotation.*;
import ru.unlegit.resthooks.executor.RequestExecutor;
import ru.unlegit.resthooks.json.JsonCodec;
import ru.unlegit.resthooks.parameter.BodyParameter;
import ru.unlegit.resthooks.parameter.HeaderParameter;
import ru.unlegit.resthooks.parameter.ParamParameter;
import ru.unlegit.resthooks.parameter.RestParameter;
import ru.unlegit.resthooks.util.Pair;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings("unchecked")
public class RestHookProxyFactory {

    public <T> T createRequesterProxy(
            @NonNull Class<T> hookInterface,
            @NonNull RequestExecutor requestExecutor,
            @NonNull JsonCodec jsonCodec
    ) {
        assert (hookInterface.isInterface());

        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{hookInterface},
                new RestInvocationHandler(
                        resolveRestData(hookInterface, jsonCodec),
                        requestExecutor
                )
        );
    }

    private static RestHookData resolveRestData(
            Class<?> hookType,
            JsonCodec jsonCodec
    ) {
        return new RestHookData(
                resolveUrl(hookType),
                resolveHeaders(hookType),
                resolveEndPoints(hookType, jsonCodec)
        );
    }

    private static String resolveUrl(Class<?> hookType) {
        String protocol = hookType.isAnnotationPresent(UseSecureProtocol.class) ? "https" : "http";
        String url = protocol + "://";

        RequestPath path = hookType.getAnnotation(RequestPath.class);

        if (path != null) {
            url += path.value();
        }

        return url;
    }

    private static Pair<String, String>[] resolveHeaders(Class<?> hookType) {
        return Arrays.stream(hookType.getAnnotationsByType(RequestHeader.class))
                .map(header -> new Pair<>(header.name(), header.value()))
                .toArray(Pair[]::new);
    }

    private static Map<String, EndPointData> resolveEndPoints(
            Class<?> hookType,
            JsonCodec jsonCodec
    ) {
        return Arrays.stream(hookType.getDeclaredMethods())
                .collect(Collectors.toMap(
                        Method::getName,
                        method -> resolveEndPoint(method, jsonCodec),
                        (first, second) -> second
                ));
    }

    private static EndPointData resolveEndPoint(
            Method method,
            JsonCodec jsonCodec
    ) {
        Class<?> returnType = method.getReturnType();

        boolean wrapResponse = returnType == RestResponse.class;
        Class<?> responseType;

        if (wrapResponse) {
            responseType = (Class<?>) ((ParameterizedType) (method.getGenericReturnType()))
                    .getActualTypeArguments()[0];

            if (responseType == Void.class) {
                responseType = void.class;
            }
        } else {
            responseType = returnType;
        }

        return new EndPointData(
                Optional.ofNullable(method.getAnnotation(RequestPath.class))
                        .map(RequestPath::value)
                        .orElse(""),
                Objects.requireNonNull(method.getAnnotation(RequestMethod.class)).value(),
                resolveParameters(method, jsonCodec),
                responseType,
                wrapResponse
        );
    }

    private static RestParameter[] resolveParameters(
            Method method,
            JsonCodec jsonCodec
    ) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> resolveParameter(parameter, jsonCodec))
                .toArray(RestParameter[]::new);
    }

    private static RestParameter resolveParameter(
            Parameter parameter,
            JsonCodec jsonCodec
    ) {
        if (parameter.isAnnotationPresent(Path.class)) {
            return (argument, request) -> request.appendPath(argument.toString());
        } else if (parameter.isAnnotationPresent(Param.class)) {
            return new ParamParameter(parameter.getAnnotation(Param.class).value());
        } else if (parameter.isAnnotationPresent(Header.class)) {
            return new HeaderParameter(parameter.getAnnotation(Header.class).value());
        } else if (parameter.isAnnotationPresent(Body.class)) {
            return new BodyParameter(jsonCodec);
        }
        throw new Error("invalid request parameter " + parameter.getName());
    }
}