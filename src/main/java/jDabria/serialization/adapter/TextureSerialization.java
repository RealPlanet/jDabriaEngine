package jDabria.serialization.adapter;

import com.google.gson.*;
import jDabria.assetManager.AssetPool;
import jDabria.assetManager.resources.Texture;

import java.lang.reflect.Type;

public class TextureSerialization implements JsonDeserializer<Texture> {

    @Override
    public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String filepath = jsonObject.get("filepath").getAsString();
        return AssetPool.getTexture(filepath);
    }
}
