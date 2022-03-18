package jDabria.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jDabria.ECP.base.Component;
import jDabria.ECP.GameObject;
import jDabria.sceneManager.Scene;
import jDabria.serialization.adapter.ComponentSerialization;
import jDabria.serialization.adapter.GameObjectSerialization;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GameObjectWR {
    private static Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .registerTypeAdapter(Component.class, new ComponentSerialization())
                                .registerTypeAdapter(GameObject.class, new GameObjectSerialization())
                                .enableComplexMapKeySerialization()
                                .create();

    public static void Write(@NotNull List<GameObject> objects, String fileName){
        try {
            FileWriter writer = new FileWriter(fileName + ".json");
            writer.write(gson.toJson(objects));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void Read(Scene scene, String filename){
        String serializedContents = "";
        try {
            serializedContents = new String(Files.readAllBytes(Paths.get(filename+ ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!serializedContents.equals("")) {
            GameObject[] objects = gson.fromJson(serializedContents, GameObject[].class);
            for (int i=0; i < objects.length; i++) {
                scene.addGameObjectToScene(objects[i]);
            }
        }
    }
}
