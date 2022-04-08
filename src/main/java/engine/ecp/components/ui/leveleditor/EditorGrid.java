package engine.ecp.components.ui.leveleditor;

import commons.Color;
import commons.util.Settings;
import engine.ecp.base.Component;
import engine.renderer.debug.DebugDraw;
import engine.scenemanager.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EditorGrid extends Component {

    @Override
    public void update(){
        Vector3f cameraPos = SceneManager.getActiveCamera().getPosition();
        Vector2f projectionSize = SceneManager.getActiveCamera().getProjectionSize();

        int firstX = (int)(cameraPos.x / Settings.GRID_WIDTH - 1) * Settings.GRID_HEIGHT;
        int firstY = (int)(cameraPos.y / Settings.GRID_HEIGHT - 1) * Settings.GRID_HEIGHT;

        int verticalLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int horizontalLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(verticalLines, horizontalLines);
        for(int i = 0; i < maxLines; i++){
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if(i < verticalLines){
                DebugDraw.drawFrameLine2D(new Vector3f(x, firstY, 0), new Vector3f(x, firstY + height, 0), Color.BLACK, 1);
            }

            if(i < horizontalLines){
                DebugDraw.drawFrameLine2D(new Vector3f(firstX, y, 0), new Vector3f(firstX + width, y, 0), Color.BLACK, 1);
            }
        }
    }
}
