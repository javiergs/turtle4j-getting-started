package examples;

import com.fasterxml.jackson.databind.JsonNode;
import com.javiergs.turtle4j.TurtleClient;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Draws a square in Turtlesim using pose feedback instead of timed turns.
 * The turtle moves forward a measured distance, then turns 90 degrees.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class SquarePath {
    private static final double SIDE_LENGTH = 2.0;
    private static final double DISTANCE_TOLERANCE = 0.05;
    private static final double ANGLE_TOLERANCE = 0.025;
    private static final long UPDATE_MS = 40;
    private static final long TIMEOUT_MS = 15000;

    /**
     * Draws four sides using Turtlesim pose messages to measure progress.
     *
     * @param args command-line arguments (unused)
     * @throws Exception if communication fails or pose feedback times out
     */
    public static void main(String[] args) throws Exception {
        AtomicReference<JsonNode> latestPose = new AtomicReference<>();

        TurtleClient turtle = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            if (!ExampleSupport.requireVelocity(turtle.connect())
                    || !ExampleSupport.requirePose(turtle.getCapabilities())) return;
            turtle.subscribe("/turtle1/pose", "turtlesim/msg/Pose", latestPose::set);
            waitForPose(latestPose);

            for (int side = 0; side < 4; side++) {
                JsonNode start = requirePose(latestPose);
                double startX = start.path("x").asDouble();
                double startY = start.path("y").asDouble();
                long deadline = System.currentTimeMillis() + TIMEOUT_MS;

                while (true) {
                    JsonNode pose = requirePose(latestPose);
                    double traveled = Math.hypot(
                            pose.path("x").asDouble() - startX,
                            pose.path("y").asDouble() - startY);
                    double remaining = SIDE_LENGTH - traveled;
                    if (remaining <= DISTANCE_TOLERANCE) break;
                    checkDeadline(deadline, "forward motion");
                    turtle.setVelocity( Math.min(0.8, Math.max(0.12, remaining * 1.5)), 0.0);
                    Thread.sleep(UPDATE_MS);
                }
                turtle.stop();
                Thread.sleep(200);

                double startHeading = requirePose(latestPose).path("theta").asDouble();
                double accumulatedTurn = 0.0;
                double previousHeading = startHeading;
                deadline = System.currentTimeMillis() + TIMEOUT_MS;

                while (true) {
                    double heading = requirePose(latestPose).path("theta").asDouble();
                    accumulatedTurn += normalizeAngle(heading - previousHeading);
                    previousHeading = heading;
                    double remaining = Math.PI / 2.0 - accumulatedTurn;
                    if (remaining <= ANGLE_TOLERANCE) break;
                    checkDeadline(deadline, "rotation");
                    turtle.setVelocity( 0.0, Math.min(0.8, Math.max(0.12, remaining * 1.5)));
                    Thread.sleep(UPDATE_MS);
                }
                turtle.stop();
                Thread.sleep(200);
            }
        } finally {
            try {
                turtle.stop();
            } catch (RuntimeException ignored) {
                // Connection may already be closed.
            }
            turtle.close();
        }
    }

    private static JsonNode requirePose(AtomicReference<JsonNode> latestPose) {
        JsonNode pose = latestPose.get();
        if (pose == null || !pose.has("x") || !pose.has("y") || !pose.has("theta")) {
            throw new IllegalStateException("No valid Turtlesim pose received");
        }
        return pose;
    }

    private static void waitForPose(AtomicReference<JsonNode> latestPose)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + TIMEOUT_MS;
        while (latestPose.get() == null) {
            checkDeadline(deadline, "initial pose");
            Thread.sleep(UPDATE_MS);
        }
        requirePose(latestPose);
    }

    private static void checkDeadline(long deadline, String operation) {
        if (System.currentTimeMillis() >= deadline) {
            throw new IllegalStateException("Timed out waiting for Turtlesim " + operation);
        }
    }

    private static double normalizeAngle(double angle) {
        return Math.atan2(Math.sin(angle), Math.cos(angle));
    }
}
