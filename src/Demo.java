import model.Resource;
import service.HandoverService;
import service.RequestService;
import service.ResourceService;
import util.AppConstants;

public class Demo {
    public static void main(String[] args) {
        ResourceService rs = new ResourceService();
        RequestService qs = new RequestService(rs);
        HandoverService hs = new HandoverService(qs, rs);

        rs.addResource(new Resource("R1", "Calculator", "scientific",
                "Electronics", "O1", 1, AppConstants.RESOURCE_STATUS_AVAILABLE));
        System.out.println("Step 1: Owner O1 listed a Calculator (quantity 1)");

        System.out.println("Step 2: Search 'calc' finds " + rs.searchResources("calc").size() + " item");
        System.out.println("        Search 'calc' in Books finds " + rs.searchResources("calc", "Books").size() + " items");

        qs.submitRequest("S1", "R1");
        qs.submitRequest("S2", "R1");
        System.out.println("Step 3: S1 and S2 both requested it (slips Q1 and Q2)");

        System.out.println("Step 4: Owner accepts Q1 -> " + qs.accept("Q1", "O1"));
        System.out.println("        Owner accepts Q2 -> " + qs.accept("Q2", "O1") + " (only 1 Calculator)");
        System.out.println("        Owner declines Q2 -> " + qs.decline("Q2", "O1"));

        String code = hs.generateCode("Q1", "O1");
        System.out.println("Step 5: Owner generates the handover code: " + code);

        System.out.println("Step 6: S1 types a wrong code -> " + hs.verify("Q1", "S1", "0000x"));
        System.out.println("        S2 types the right code -> " + hs.verify("Q1", "S2", code));
        System.out.println("        S1 types the right code -> " + hs.verify("Q1", "S1", code));
        System.out.println("        S1 types it again (reuse) -> " + hs.verify("Q1", "S1", code));

        Resource r = rs.findById("R1");
        System.out.println("Step 7: Calculator now has quantity " + r.getQuantity() + ", status " + r.getStatus());

        System.out.println("Step 8: History of Q1: " + qs.getTracker().getHistory("Q1"));
        System.out.println("        Can COMPLETED go back to PENDING? "
                + qs.getTracker().canMoveTo("COMPLETED", "PENDING"));
    }
}