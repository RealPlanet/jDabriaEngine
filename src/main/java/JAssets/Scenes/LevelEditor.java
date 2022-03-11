package JAssets.Scenes;

import Commons.Color;
import Commons.Time;
import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.GameObject;
import JDabria.Renderer.Camera;
import JDabria.SceneManager.Scene;
import JDabria.Window;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void Init() {
        this._Camera = new Camera(new Vector3f(0, 0f,0f));
        Window.SetWindowClearColor(new Color(0,0,0,1));

        int xOffset = 0, yOffset = 0;
        float TotalWidth = (float)(1920 - xOffset * 2);
        float TotalHeight = (float)(1080 - yOffset * 2);

        int SizeX = (int) (TotalWidth / 100);
        int SizeY = (int) (TotalHeight / 100);
        float Padding = 0;

        for(int x = 0; x < 100; x++){
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * SizeX) + (x * Padding);
                float yPos = yOffset + (y * SizeY) + (y * Padding);

                GameObject go = new GameObject("Obj" + x + "" + y,
                                                new Vector3f(xPos, yPos, 0f),
                                                new Vector3f(SizeX, SizeY, 1f));

                go.AddComponent(new SpriteRenderer(new Color(xPos / TotalWidth, yPos / TotalHeight, 1, 1)));
                AddGameObjectToScene(go);
            }
        }
    }

    @Override
    protected void Update() {
        System.out.println(String.format("FPS :: %f", 1f / Time.DeltaTime()));
        _Renderer.Render();
    }
}
