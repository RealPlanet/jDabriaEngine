package jDabria.serialization.adapter;

import com.google.gson.*;
import jDabria.ECP.GameObject;
import jDabria.ECP.base.Component;

import java.lang.reflect.Type;

public class GameObjectSerialization implements JsonDeserializer<GameObject>{
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject go = new GameObject(name);
        for (JsonElement e : components) {
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }

        //go.setTransform(go.getComponent(Transform.class));
        return go;
    }
}
