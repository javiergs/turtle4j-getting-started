package examples;

import com.javiergs.turtle4j.RobotCapabilities;
import com.javiergs.turtle4j.RosInterface;
import com.javiergs.turtle4j.TurtleCapability;
import com.javiergs.turtle4j.TurtleClient;

/**
 * Common capability checks for the runnable examples.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
final class ExampleSupport {
    static final String ENDPOINT = "ws://localhost:9090";
    private static final String POSE_TOPIC = "/turtle1/pose";
    private static final String POSE_TYPE = "turtlesim/msg/Pose";

    private ExampleSupport() { }

    static boolean requireVelocity(RobotCapabilities report) {
        if (report.isAvailable(TurtleCapability.VELOCITY)) return true;
        System.out.println("Cannot run: VELOCITY is "
                + report.getAvailability(TurtleCapability.VELOCITY)
                + ". Check Turtlesim, rosapi, and the ROS 2 topic graph.");
        if (report.getError() != null) System.out.println("Discovery: " + report.getError());
        return false;
    }

    static boolean requirePose(RobotCapabilities report) {
        if (report.getDiscoveryStatus() != RobotCapabilities.DiscoveryStatus.COMPLETE) {
            System.out.println("Cannot run: pose topic cannot be verified because discovery is UNKNOWN.");
            return false;
        }
        for (RosInterface item : report.getExtras()) {
            if (POSE_TOPIC.equals(item.getName()) && POSE_TYPE.equals(item.getType())) return true;
        }
        System.out.println("Cannot run: Turtlesim pose topic " + POSE_TOPIC
                + " (" + POSE_TYPE + ") was not discovered.");
        return false;
    }

    static void stopQuietly(TurtleClient client) {
        try {
            client.stop();
        } catch (RuntimeException ex) {
            System.err.println("Could not publish stop command: " + ex.getMessage());
        }
    }
}
