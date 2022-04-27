package engine.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commons.util.StringUtils;
import commons.util.io.FileUtil;
import engine.assetmanager.resources.Texture;
import engine.ecp.GameObject;
import engine.ecp.base.Component;
import engine.scenemanager.Scene;
import engine.serialization.adapter.ComponentSerialization;
import engine.serialization.adapter.GameObjectSerialization;
import engine.serialization.adapter.TextureSerialization;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameSerialize {
    private final Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .registerTypeAdapter(Component.class, new ComponentSerialization())
                                .registerTypeAdapter(GameObject.class, new GameObjectSerialization())
                                .registerTypeAdapter(Texture.class, new TextureSerialization())
                                .enableComplexMapKeySerialization()
                                .create();

    private CompileLevel outputCompileLevel = CompileLevel.NONE;
    private CompressionLevel outputCompressionLevel = CompressionLevel.NONE;

    public enum CompileLevel{
        NONE,
        OBFUSCATE
    }

    public enum CompressionLevel{
        NONE,
        DEFAULT
    }

    public GameSerialize(){}

    public GameSerialize setCompression(CompressionLevel level){
        outputCompressionLevel = level;
        return this;
    }

    public GameSerialize setCompile(CompileLevel level){
        outputCompileLevel = level;
        return this;
    }

    public void write(@NotNull List<GameObject> objects, String fileName){
        String json = gson.toJson(objects);
        Path filePath = Paths.get(fileName + ".json");

        if(outputCompileLevel == CompileLevel.OBFUSCATE){
            json = StringUtils.encode64(json);
        }

        if(json == null){
            return;
        }

        if(outputCompressionLevel == CompressionLevel.DEFAULT){
            byte[] compressed = StringUtils.compressString(json);
            FileUtil.WriteToFile(compressed, filePath);
            return;
        }

        FileUtil.WriteToFile(filePath, json);
    }

    public void read(Scene scene, String fileName){
        Path filePath = Paths.get(fileName + ".json");
        String serializedContents;

        if (outputCompressionLevel == CompressionLevel.DEFAULT) {
            serializedContents = new String(StringUtils.decompressBytes(FileUtil.readBytesFromFile(filePath)));
        } else {
            serializedContents = FileUtil.readFromFile(filePath);
        }

        if(outputCompileLevel == CompileLevel.OBFUSCATE){
            serializedContents = StringUtils.decode64(serializedContents);
        }

        //long maxComponentCount = 0;
        //long maxGameobjectCount = 0;
        GameObject[] objects = FileUtil.jsonSafeParse(gson, GameObject[].class, serializedContents);

        if (objects != null){
            for (GameObject object : objects) {
                scene.addGameObjectToScene(object);
               // maxComponentCount += object.getAllComponents().size();
               // maxGameobjectCount++;
            }
        }
    }
}
