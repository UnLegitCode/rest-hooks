package ru.unlegit.resthooks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class FilteringInvocationHandler implements InvocationHandler {

    private static final String[] OBJECT_METHODS = {
            "clone", "equals", "hashCode",
            "toString", "getClass", "finalize",
            "notify", "notifyAll", "wait",
    };

    private static boolean filter(String methodName) {
        for (String objectMethod : OBJECT_METHODS) {
            if (methodName.equals(objectMethod)) return false;
        }
        return true;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();

        return filter(methodName) ? invoke(proxy, methodName, args) : null;
    }

    protected abstract Object invoke(Object proxy, String methodName, Object[] args);
}