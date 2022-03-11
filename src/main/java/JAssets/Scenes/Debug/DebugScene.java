package JAssets.Scenes.Debug;


import Commons.Color;
import Commons.Time;
import JDabria.KeyListener;
import JDabria.SceneManager.Scene;
import JDabria.SceneManager.SceneManager;
import JDabria.Window;

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
    public void Init() {

    }

    @Override
    public void OnFrameUpdate() {
        if(!Fading && KeyListener.IsKeyPressed(GLFW_KEY_SPACE)){
            Fading = true;
        }

        if(KeyListener.IsKeyPressed(GLFW_KEY_ESCAPE)){
            System.out.println(String.format("%f FPS", 1.0f / Time.DeltaTime()));
        }

        if(Fading){
            if(FadeCurrentTime > 0){
                float Delta = Time.DeltaTime();
                FadeCurrentTime -= Delta;
                Color CurrentFade = new Color(   FadeStart.GetRed() * (FadeCurrentTime / FadeDurationTime),
                                                 FadeStart.GetGreen() * (FadeCurrentTime / FadeDurationTime),
                                                 FadeStart.GetBlue() * (FadeCurrentTime / FadeDurationTime),
                                                    FadeStart.GetAlpha());
                Window.SetWindowClearColor(CurrentFade);
                return;
            }

            SceneManager.LoadScene("LevelEditor", SceneManager.LoadType.SINGLE);
        }
    }
}
