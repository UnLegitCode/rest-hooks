package ru.unlegit.resthooks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.unlegit.resthooks.executor.RequestExecutor;
import ru.unlegit.resthooks.parameter.RestParameter;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestInvocationHandler extends FilteringInvocationHandler {

    RestHookData hookData;
    RequestExecutor requestExecutor;

    @Override
    protected Object invoke(Object proxy, String methodName, Object[] args) {
        EndPointData endPoint = hookData.getEndPoint(methodName);

        assert(endPoint != null);

        RestRequest request = createRequest(endPoint);

        resolveArgs(request, endPoint.getParameters(), args);

        return requestExecutor.executeRequest(request);
    }

    private RestRequest createRequest(EndPointData endPoint) {
        RestRequest request = new RestRequest(endPoint.getMethod(), hookData.getPath());

        request.appendPath(endPoint.getPath());
        request.addHeaders(hookData.getHeaders());
        request.setResponseType(endPoint.getResponseType());
        request.setWrapResponse(endPoint.isWrapResponse());

        return request;
    }

    private void resolveArgs(RestRequest request, RestParameter[] params, Object[] args) {
        assert(params.length == args.length);

        for (int i = 0; i < params.length; i++) {
            params[i].resolve(args[i], request);
        }
    }
}