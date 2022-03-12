import Commons.Time;
import JDabria.Window;

public class Main {
    public static void main(String[] args){
        System.out.println("Application started at" + (Time.getApplicationStartTime()) + " Seconds");
        Window window = Window.getWindow();
        System.out.println("Application ran for " + (Time.getTime()) + " Seconds");

        window.run();
    }
}
