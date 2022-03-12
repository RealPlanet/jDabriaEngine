package JAssets.Scenes.Debug;


import Commons.Color;
import Commons.Time;
import JDabria.ECP.Components.Sprite.SpriteRenderer;
import JDabria.ECP.GameObject;
import JDabria.keyListener;
import JDabria.SceneManager.Scene;
import JDabria.SceneManager.SceneManager;
import JDabria.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class DebugScene extends Scene {
    private boolean Fading = false;
    private final float FadeDurationTime = 2.0f;
    private float FadeCurrentTime = FadeDurationTime;

    private final Color FadeStart = new Color(1, 1, 1, 1);
    private Color CurrentFade = FadeStart;
    Color c = new Color();

    public DebugScene(){
        System.out.println("Inside Debug Scene!");
    }

    @Override
    public void onInit() {
        Window.setWindowClearColor(new Color(0,0,0,1));
        int xOffset = 10, yOffset = 10;
        float TotalWidth = (float)(1200 - xOffset * 2);
        float TotalHeight = (float)(600 - yOffset * 2);

        int NumSquaresX = 100;
        int NumSquaresY = 100;
        int Padding = 3;

        int SizeX = (int) (TotalWidth / (NumSquaresX - 1));
        int SizeY = (int) (TotalHeight / (NumSquaresY - 1));

        for(int x = 0; x < NumSquaresX; x++){
            for (int y = 0; y < NumSquaresY; y++) {
                float xPos = xOffset + (x * SizeX) + x * Padding;
                float yPos = yOffset + (y * SizeY) + y * Padding;

                GameObject go = new GameObject("Obj" + x + "" + y,
                        new Vector3f(xPos, yPos, 0f),
                        new Vector3f(SizeX, SizeY, 1f));

                go.addComponent(new SpriteRenderer(new Color(xPos / TotalWidth, yPos / TotalHeight, 1, 1)));
                addGameObjectToScene(go);
            }
        }
    }

    @Override
    protected void update() {

        if(!Fading && keyListener.isKeyPressed(GLFW_KEY_SPACE)){
            Fading = true;
        }

        if(keyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            System.out.println(String.format("%f FPS", 1.0f / Time.deltaTime()));
        }

        if(Fading){
            if(FadeCurrentTime > 0){
                float Delta = Time.deltaTime();
                FadeCurrentTime -= Delta;
                Color CurrentFade = new Color(   FadeStart.getRed() * (FadeCurrentTime / FadeDurationTime),
                        FadeStart.getGreen() * (FadeCurrentTime / FadeDurationTime),
                        FadeStart.getBlue() * (FadeCurrentTime / FadeDurationTime),
                        FadeStart.getAlpha());
                Window.setWindowClearColor(CurrentFade);
                return;
            }

            SceneManager.loadScene("LevelEditor", SceneManager.LoadType.SINGLE);
        }
    }

    @Override
    protected void loadResources() {

    }


}
