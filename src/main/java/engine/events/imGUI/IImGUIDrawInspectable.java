package engine.events.imGUI;

import java.io.Serializable;

@FunctionalInterface
public interface IImGUIDrawInspectable extends Serializable {
    void draw();
}
