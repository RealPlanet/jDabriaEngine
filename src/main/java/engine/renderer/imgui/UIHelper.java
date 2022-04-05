package engine.renderer.imgui;

import commons.StringUtils;
import commons.logging.EngineLogger;
import imgui.ImGui;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UIHelper {

    /**
     * Unlocks a field if it's private
     * NOTICE :: It is your responsability to lock the field again once you're finished, with either {@link UIHelper} or manually
     * @param field the field to check and/or make public
     * @return True if the field was private
     */
    public static boolean openIfPrivate(@NotNull Field field){
        boolean isPrivate = Modifier.isPrivate(field.getModifiers());
        if(isPrivate){
            field.setAccessible(true);
        }

        return isPrivate;
    }

    /**
     * restores a modifier accessibility value based on the passed value
     * @param field The field to restore
     * @param wasPrivate true if the field was private and needs to be locked again
     */
    public static void restoreModifier(@NotNull Field field, boolean wasPrivate){
        field.setAccessible(wasPrivate);
    }

    public static <T> void dragField(T Owner, Field field, String label){
        Class<?> fieldType = field.getType();
        if(fieldType == int.class){
            dragInt(Owner, field, fieldType, label);
            return;
        }

        if(fieldType == float.class){
            dragFloat(Owner, field, fieldType, label);
            return;
        }

        if(fieldType == Vector3f.class){
            dragVector3f(Owner, field, fieldType, label);
            return;
        }

        EngineLogger.logWarning(StringUtils.format("Could not find drag field for type {0}", fieldType));
    }

    public static <T> void dragFloat(T Owner, @NotNull Field field, Class<?> fieldType, String label){
        try{
            Object fieldValue = field.get(Owner);
            float[] imFloat = {(float)fieldValue};
            if(ImGui.dragFloat(label, imFloat)){
                    field.set(Owner, imFloat[0]);
            }
        }
        catch(IllegalAccessException ex){
            EngineLogger.logError(StringUtils.format("Could not get field value, was field private? {0}::{1}", Owner.getClass(), label));
        }
    }

    public static <T> void dragVector3f(T Owner, @NotNull Field field, Class<?> fieldType, String label){
        try{
            Object fieldValue = field.get(Owner);
            Vector3f value = (Vector3f)fieldValue;
            float[] imFloat = {value.x, value.y, value.z};
            if(ImGui.dragFloat3(label, imFloat)){
                value.set(imFloat[0], imFloat[1], imFloat[2]);
            }
        }
        catch(IllegalAccessException ex){
            EngineLogger.logError(StringUtils.format("Could not get field value, was field private? {0}::{1}", Owner.getClass(), label));
        }
    }

    public static <T> void dragInt(T Owner, @NotNull Field field, Class<?> fieldType, String label){
        try{
            Object fieldValue = field.get(Owner);
            int[] imInt = {(int)fieldValue};
            if(ImGui.dragInt(label, imInt)){
                field.set(Owner, imInt[0]);
            }
        }
        catch(IllegalAccessException ex){
            EngineLogger.logError(StringUtils.format("Could not get field value, was field private? {0}::{1}", Owner.getClass(), label));
        }
    }

    public static <T> void checkBox(T Owner, @NotNull Field field, String label){
        if(field.getType() != boolean.class){
            EngineLogger.logWarning(StringUtils.format("Cannot created a checkbox from type {0}", field.getType()));
            return;
        }

        try{
            Object fieldValue = field.get(Owner);
            boolean value = (boolean)fieldValue;
            if(ImGui.checkbox(label, value)){
                field.set(Owner, !value);
            }
        }
        catch(IllegalAccessException ex){
            EngineLogger.logError(StringUtils.format("Could not get field value, was field private? {0}::{1}", Owner.getClass(), label));
        }
    }

}
