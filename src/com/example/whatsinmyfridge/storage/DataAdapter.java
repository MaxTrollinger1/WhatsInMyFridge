package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.storage.data.Data;
import com.google.gson.*;
import java.lang.reflect.Type;

/// Third party class to handle polymorphism of serialized classes
public class DataAdapter implements JsonSerializer<Data>, JsonDeserializer<Data> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public JsonElement serialize(Data src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty(CLASSNAME, src.getClass().getName());

        Gson gsonWithoutAdapter = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        JsonElement instanceElement = gsonWithoutAdapter.toJsonTree(src);
        result.add(INSTANCE, instanceElement);

        return result;
    }

    @Override
    public Data deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        System.out.println("Adapter deserializing: " + json);
        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object, got: " + json);
        }

        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement classNameElement = jsonObject.get(CLASSNAME);

        if (classNameElement == null) {
            throw new JsonParseException("Missing CLASSNAME field in JSON: " + json);
        }

        String className = classNameElement.getAsString();
        System.out.println("Class to instantiate: " + className);

        JsonElement instanceElement = jsonObject.get(INSTANCE);
        if (instanceElement == null) {
            throw new JsonParseException("Missing INSTANCE field in JSON: " + json);
        }

        System.out.println("Instance JSON: " + instanceElement);

        try {
            Class<?> clazz = Class.forName(className);

            // Use a new Gson instance without this adapter to avoid recursion
            Gson gsonWithoutAdapter = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            Data result = (Data) gsonWithoutAdapter.fromJson(instanceElement, clazz);
            System.out.println("Deserialized result: " + (result != null ? result.getClass().getSimpleName() : "null"));
            return result;

        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown class: " + className, e);
        }
    }
}