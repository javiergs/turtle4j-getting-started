package examples;

import com.javiergs.turtle4j.RobotCapabilities;
import com.javiergs.turtle4j.TurtleCapability;
import com.javiergs.turtle4j.TurtleClient;

/**
 * Prints the live rosapi discovery snapshot and capability availability.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class DiscoverRobot {
    public static void main(String[] args) throws Exception {
        TurtleClient client = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            RobotCapabilities report = client.connect();
            System.out.println("Discovery: " + report.getDiscoveryStatus());
            if (report.getError() != null) System.out.println("Reason: " + report.getError());
            for (TurtleCapability capability : TurtleCapability.values()) {
                System.out.println(capability + ": " + report.getAvailability(capability)
                        + " " + report.getInterface(capability));
            }
            System.out.println("Other topics: " + report.getExtras());
            System.out.println("Services: " + report.getServices());
        } finally {
            client.close();
        }
    }
}
