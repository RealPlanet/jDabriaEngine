package commons;

import org.joml.Vector3f;

public class Utils {
    public static boolean isIntOrFloat(Class<?> clazz){
        return  clazz == int.class ||
                clazz == Integer.class ||
                clazz == float.class ||
                clazz == Float.class;
    }

    public static boolean isBoolean(Class<?> clazz){
        return clazz == boolean.class;
    }

    public static boolean isVector3f(Class<?> clazz){
        return clazz == Vector3f.class;
    }
}
