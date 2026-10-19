import java.util.List;
import model.Resource;
import service.ResourceService;
public class Main {
    public static void main(String[] args) {
        ResourceService rs=new ResourceService();
        rs.addResource(new Resource("Calculator",1,true,"Electronics"));
        rs.addResource(new Resource("Java Book",2,true,"Book"));
        rs.addResource(new Resource("Lab Coat",3,false,"Clothing"));
        List<Resource> result= rs.searchResources("o");
        for (Resource r:result)
        {
            System.out.println(r.getName());
        }
    }
}