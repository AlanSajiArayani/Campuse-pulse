import model.Resource;
import model.ResourceRequest;
import service.HandoverService;
import service.RequestService;
import service.ResourceService;
import util.AppConstants;

public class Test {
    public static void main(String[] args) {
        ResourceService rs = new ResourceService();
        RequestService qs = new RequestService(rs);
        HandoverService hs = new HandoverService(qs, rs);

        rs.addResource(new Resource("R1", "Calculator", "scientific",
                "Electronics", "O1", 1, AppConstants.RESOURCE_STATUS_AVAILABLE));

        ResourceRequest q1 = qs.submitRequest("S1", "R1");
        ResourceRequest q2 = qs.submitRequest("S2", "R1");

        System.out.println(qs.accept("Q1", "O1"));
        System.out.println(qs.accept("Q2", "O1"));

        String code = hs.generateCode("Q1", "O1");
        System.out.println(code);

        System.out.println(hs.verify("Q1", "S1", "wrong"));
        System.out.println(hs.verify("Q1", "S1", code));
        System.out.println(hs.verify("Q1", "S1", code));

        Resource r = rs.findById("R1");
        System.out.println(r.getQuantity() + " " + r.getStatus());
        System.out.println(q1.getStatus());
    }
}