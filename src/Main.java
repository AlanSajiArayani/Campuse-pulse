import controller.MainController;
import view.MainView;

/**
 * Main.java
 * Application entry point for CampusPulse.
 *
 * TODO [Alan Saji]   – Wire up database/session initialization before controller.start()
 * TODO [Ananya Raju] – Ensure MainView is fully set up before being passed to the controller
 */
public class Main {

    public static void main(String[] args) {
        // TODO [Alan Saji] – Add pre-startup initialization (config loading, DB connection, etc.)

        MainView       view       = new MainView();
        MainController controller = new MainController(view);

        view.setController(controller);
       // Launch the application
        controller.start();
    }
}

