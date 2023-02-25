package ru.unlegit.resthooks.json;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GsonCodec implements JsonCodec {

    private final Gson gson;

    @Override
    public String encode(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Object decode(String json, Class<?> objectType) {
        return gson.fromJson(json, objectType);
    }
}