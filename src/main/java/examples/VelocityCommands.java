package examples;

import com.javiergs.turtle4j.RosbridgeConnection;
import java.util.Map;

/**
 * Example-only utility for publishing Twist velocity commands to any topic.
 * TURTLE4J itself remains independent of robot-specific motion topics.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
final class VelocityCommands {
    private VelocityCommands() {
    }

    /** Publishes a linear and angular velocity command.
     * @param connection active rosbridge connection
     * @param topic ROS 2 topic accepting geometry_msgs/msg/Twist
     * @param linear forward velocity in meters per second
     * @param angular angular velocity in radians per second
     */
    static void move(RosbridgeConnection connection, String topic, double linear, double angular) {
        connection.publish(topic, "geometry_msgs/msg/Twist", Map.of(
                "linear", Map.of("x", linear, "y", 0.0, "z", 0.0),
                "angular", Map.of("x", 0.0, "y", 0.0, "z", angular)));
    }

    /** Publishes a zero-velocity command to the specified topic.
     * @param connection active rosbridge connection
     * @param topic ROS 2 topic accepting geometry_msgs/msg/Twist
     */
    static void stop(RosbridgeConnection connection, String topic) {
        move(connection, topic, 0.0, 0.0);
    }
}
