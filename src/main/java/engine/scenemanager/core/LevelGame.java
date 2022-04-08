package engine.scenemanager.core;

import commons.util.logging.EngineLogger;
import engine.scenemanager.Scene;
import engine.Engine;

public class LevelGame extends Scene {
    public LevelGame(){
        EngineLogger.log("Entering GAME");
    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onUnload() {

    }

    @Override
    protected void onClear() {

    }

    @Override
    protected void loadResources() {

    }

    @Override
    public void onInit() {
        Engine.getEngine().setState(Engine.State.PLAY_MODE);
    }
}
