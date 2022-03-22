import commons.logging.EngineLogger;
import jDabria.Window;

public class Main {
    public static void main(String[] args){
        EngineLogger.open();

        Window window = Window.getWindow();
        window.run();

        EngineLogger.close();
    }
}
