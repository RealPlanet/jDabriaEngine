package engine.serialization.adapter;

import com.google.gson.*;
import engine.ecp.GameObject;
import engine.ecp.base.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.UUID;

public class GameObjectSerialization implements JsonDeserializer<GameObject>{
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonElement UID = jsonObject.get("UID");

        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject go = new GameObject(name);
        try {
            Field gameObjectUUID = GameObject.class.getDeclaredField("UID");
            gameObjectUUID.setAccessible(true);

            UUID uuid = context.deserialize(UID, UUID.class);
            gameObjectUUID.set(go, uuid);
            gameObjectUUID.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (JsonElement e : components) {
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }

        return go;
    }
}
