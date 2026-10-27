package examples;

import com.javiergs.turtle4j.TurtleClient;

/** Print incoming Turtlesim pose messages for ten seconds. 
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class ReadPose {
    /** Runs the ReadPose example against a local Turtlesim instance.
     * @param args command-line arguments (unused)
     * @throws Exception if ROS communication or timed execution fails
     */
    public static void main(String[] args) throws Exception {
        TurtleClient turtle = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            if (!ExampleSupport.requirePose(turtle.connect())) return;
            turtle.subscribe("/turtle1/pose", "turtlesim/msg/Pose",
                pose -> System.out.printf("x=%.2f y=%.2f theta=%.2f%n",
                    pose.path("x").asDouble(), pose.path("y").asDouble(),
                    pose.path("theta").asDouble()));
            Thread.sleep(10000);
        } finally {
            turtle.close();
        }
    }
}
