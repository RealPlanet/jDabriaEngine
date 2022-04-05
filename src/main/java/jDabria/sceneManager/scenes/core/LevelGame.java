package jDabria.sceneManager.scenes.core;

import commons.logging.EngineLogger;
import jDabria.engine.Engine;
import jDabria.sceneManager.Scene;

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
