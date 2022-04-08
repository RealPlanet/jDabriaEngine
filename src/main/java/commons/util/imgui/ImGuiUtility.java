package commons.util.imgui;

import commons.util.Utils;
import imgui.ImGui;
import engine.renderer.imgui.UIHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ImGuiUtility {
    public static <T> void drawDefaultFields(@NotNull T ref, String windowName){
        ImGui.begin(windowName);

        Field[] fields = ref.getClass().getDeclaredFields();
        for(Field field : fields) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            boolean wasPrivate = UIHelper.openIfPrivate(field);

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            if (Utils.isIntOrFloat(fieldType) || Utils.isVector3f(fieldType)) {
                UIHelper.dragField(ref, field, fieldName);
            }
            else if (Utils.isBoolean(fieldType)) {
                UIHelper.checkBox(ref, field, fieldName);
            }

            UIHelper.restoreModifier(field, wasPrivate);
        }

        ImGui.end();
    }
}
