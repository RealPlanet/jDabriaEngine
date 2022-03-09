package Assets.Scenes;

import Commons.Color;
import Commons.Time;
import JDabria.KeyListener;
import JDabria.SceneManager.Scene;
import JDabria.SceneManager.SceneManager;
import JDabria.Window;

import java.awt.event.KeyEvent;

public class LevelEditor extends Scene {
    private boolean Fading = false;
    private float FadeDuration = 2.0f;
    private final Color FadeStart = new Color();
    private final Color CurrentFade = FadeStart;

    public LevelEditor(){
        super();
    }

    @Override
    public void OnFrameUpdate() {
        if(!Fading && KeyListener.IsKeyPressed(KeyEvent.VK_SPACE)){
            Fading = true;
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
