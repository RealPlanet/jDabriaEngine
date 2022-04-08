package engine.scenemanager.core;

import commons.Color;
import commons.io.FileUtil;
import commons.math.GMath;
import engine.assetmanager.AssetPool;
import engine.ecp.GameObject;
import engine.ecp.components.Camera;
import engine.ecp.components.sprite.SpriteRenderer;
import engine.renderer.debug.DebugDrawer;
import engine.renderer.sprite.SpriteSheet;
import engine.scenemanager.Scene;
import engine.serialization.GameSerialize;
import org.joml.Vector3f;

import java.util.ArrayList;

public class LevelTestObjects extends Scene {
    private transient SpriteSheet gameSprites;

    @Override
    protected void onInit() {

        DebugDrawer.drawLine2D(new Vector3f(0, 0,0), new Vector3f(800, 800, 0), Color.RED, 2f);

        if(FileUtil.exists(LevelTestObjects.class.getCanonicalName()+ ".json")){
            new GameSerialize().read(this, LevelTestObjects.class.getCanonicalName());
            sceneCamera = findGameObjectByName("CameraObj");
            return;
        }

        sceneCamera = Camera.createDefaultCamera(this);
        ArrayList<GameObject> objects = new ArrayList<>(50);
        for(int i = 0; i < 50; i++){
            objects.add(new GameObject("Object " + i));
            objects.get(i).addComponent(new SpriteRenderer(gameSprites.getSprite((int) GMath.clamp(0, gameSprites.size(), i)).setSize(64,64)));
            objects.get(i).setPosition(new Vector3f(i * 10f, i *10f, 0));
            addGameObjectToScene(objects.get(i));
        }
    }

    double t = 0;
    @Override
    protected void onUpdate() {
        sceneRenderer.Render();

        float x = (float) (Math.sin(t) * 200.0f + 600);
        float y = (float) (Math.cos(t) * 200.0f + 400);

        t += 0.05f;
        DebugDrawer.drawLine2D(new Vector3f(600, 400, 0), new Vector3f(x, y, 0), Color.PURPLE, 0.0001f);
    }

    @Override
    protected void onUnload() {

    }

    @Override
    protected void onClear() {

    }

    @Override
    protected void loadResources() {
        AssetPool.addSpriteSheet("Assets/Textures/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("Assets/Textures/decorationsAndBlocks.png"), 16, 16, 81, 0));

        gameSprites = AssetPool.getSpriteSheet("Assets/Textures/decorationsAndBlocks.png");
    }
}
