package JAssets.Scenes;

import Commons.Time;
import JDabria.SceneManager.Scene;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void init() {
    }

    @Override
    protected void update() {
        System.out.println(String.format("FPS :: %f", 1f / Time.deltaTime()));
        sceneRenderer.Render();
    }
}
