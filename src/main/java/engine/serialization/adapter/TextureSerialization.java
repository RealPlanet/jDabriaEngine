package engine.serialization.adapter;

import com.google.gson.*;
import engine.assetmanager.AssetPool;
import engine.assetmanager.resources.Texture;

import java.lang.reflect.Type;

public class TextureSerialization implements JsonDeserializer<Texture> {

    @Override
    public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String filepath = jsonObject.get("filepath").getAsString();
        return AssetPool.getTexture(filepath);
    }
}
