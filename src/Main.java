import model.Resource;
public class main {

    public static void main(String[] args) {
        Resource r1= new Resource("Calculator", 1, true);
        Resource r2= new Resource("Java Book", 2, false);
        System.out.println(r1.isAvailable());
        System.out.println(r2.isAvailable());
    }
}