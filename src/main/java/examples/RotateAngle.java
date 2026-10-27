package examples;

import com.javiergs.turtle4j.RobotCapabilities;
import com.javiergs.turtle4j.TurtleClient;

/**
 * Demonstrates feedback-based angle control in Turtlesim.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class RotateAngle {
    public static void main(String[] args) throws Exception {
        TurtleClient client = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            RobotCapabilities report = client.connect();
            if (!ExampleSupport.requireVelocity(report) || !ExampleSupport.requirePose(report)) return;
            client.rotate(90.0);
            System.out.println("RotateAngle complete.");
        } finally {
            ExampleSupport.stopQuietly(client);
            client.close();
        }
    }
}
