package ru.unlegit.resthooks.json;

public interface JsonCodec {

    String encode(Object object);

    Object decode(String json, Class<?> objectType);
}