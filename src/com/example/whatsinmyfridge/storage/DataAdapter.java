package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.storage.data.Data;
import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * Gson adapter handling polymorphic serialization and deserialization of Data subclasses.
 * <p>
 * Inserts a CLASSNAME field alongside the serialized INSTANCE data to preserve type information.
 */
public class DataAdapter implements JsonSerializer<Data>, JsonDeserializer<Data> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    /**
     * Serializes a Data object by writing its class name and its JSON fields.
     *
     * @param src the Data instance to serialize
     * @param typeOfSrc unused type token
     * @param context unused context
     * @return JSON object containing CLASSNAME and INSTANCE entries
     */
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

    /**
     * Deserializes JSON into the appropriate Data subclass based on CLASSNAME.
     *
     * @param json JSON containing CLASSNAME and INSTANCE
     * @param typeOfT unused type token
     * @param context unused context
     * @return reconstructed Data object
     * @throws JsonParseException if JSON format is invalid or class is not found
     */
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