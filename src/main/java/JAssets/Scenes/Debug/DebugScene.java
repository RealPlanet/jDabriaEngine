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
    private float FadeDuration = 2.0f;
    private final Color FadeStart = new Color();
    private final Color CurrentFade = FadeStart;

    public DebugScene(){
        System.out.println("Inside Debug Scene!");
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
            if(FadeDuration > 0){
                float Delta = Time.DeltaTime();
                FadeDuration -= Delta;
                Color NewFade = new Color(  FadeStart.R() - Delta * 5,
                        FadeStart.G() - Delta * 5,
                        FadeStart.B() - Delta * 5,
                        1);
                CurrentFade.SetColor(NewFade);
                Window.SetWindowClearColor(CurrentFade);
                return;
            }

            SceneManager.ChangeScene("Level_1");
        }
    }

    @Override
    public void Init() {

    }


}
