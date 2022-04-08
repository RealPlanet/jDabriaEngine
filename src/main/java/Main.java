import commons.util.logging.EngineLogger;
import engine.Engine;

public class Main {
    public static void main(String[] args){
        EngineLogger.open();

        Engine engine = Engine.getEngine();
        engine.run();

        EngineLogger.close();
    }
}
