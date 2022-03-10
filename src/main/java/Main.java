import Commons.Time;
import JDabria.Window;

public class Main {
    public static void main(String[] args){
        System.out.println("Application started at" + (Time.GetApplicationStartTime()) + " Seconds");
        Window window = Window.GetWindow();
        System.out.println("Application ran for " + (Time.GetTimeSinceStartOfApplication()) + " Seconds");

        window.Run();
    }
}
