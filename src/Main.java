import controller.MainController;
import view.MainView;

public class Main {

    public static void main(String[] args) {
        MainView       view       = new MainView();
        MainController controller = new MainController(view);

        view.setController(controller);
        controller.start();
    }
}


