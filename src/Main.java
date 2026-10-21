import controller.MainController;
import view.MainView;

/**
 * Main.java
 * Application entry point for CampusPulse.
 */
public class Main {

    public static void main(String[] args) {
        MainView       view       = new MainView();
        MainController controller = new MainController(view);

        view.setController(controller);
        controller.start();
    }
}


