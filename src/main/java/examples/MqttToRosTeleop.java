package examples;

import com.javiergs.turtle4j.TurtleClient;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Educational MQTT-to-ROS 2 velocity bridge for the local Turtlesim simulator.
 * Uses Turtle4J for both MQTT subscription and ROS 2 motion commands.
 *
 * @author javiergs
 * @version 1.0 (2026-10-31)
 */
public final class MqttToRosTeleop {
    private static final long TIMEOUT_MS = 500;

    private MqttToRosTeleop() { }

    public static void main(String[] args) throws Exception {
        String rosbridge = args.length > 0 ? args[0] : ExampleSupport.ENDPOINT;
        String broker = args.length > 1 ? args[1] : "tcp://localhost:1883";
        String topic = args.length > 2 ? args[2] : "turtle4j/demo/cmd_vel";
        TurtleClient turtle = new TurtleClient(rosbridge);
        ScheduledExecutorService watchdog = Executors.newSingleThreadScheduledExecutor();
        AtomicLong lastCommand = new AtomicLong(0);
        CountDownLatch exit = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(exit::countDown));

        try {
            if (!ExampleSupport.requireVelocity(turtle.connect())) return;
            turtle.connectMqtt(broker);
            turtle.subscribeMqtt(topic, (receivedTopic, payload) -> {
                try {
                    String[] parts = payload.trim().split(",");
                    if (parts.length != 2) throw new IllegalArgumentException("Expected linear,angular");
                    double linear = Double.parseDouble(parts[0]);
                    double angular = Double.parseDouble(parts[1]);
                    if (!Double.isFinite(linear) || !Double.isFinite(angular)
                            || Math.abs(linear) > 1.0 || Math.abs(angular) > 1.0) {
                        throw new IllegalArgumentException("Velocity must be finite and within [-1, 1]");
                    }
                    turtle.setVelocity(linear, angular);
                    lastCommand.set(System.currentTimeMillis());
                    System.out.printf("MQTT -> Turtlesim: linear=%.2f angular=%.2f%n", linear, angular);
                } catch (RuntimeException e) {
                    System.err.println("Ignoring invalid command: " + e.getMessage());
                }
            });
            watchdog.scheduleAtFixedRate(() -> {
                long last = lastCommand.get();
                if (last != 0 && System.currentTimeMillis() - last > TIMEOUT_MS
                        && lastCommand.compareAndSet(last, 0)) {
                    ExampleSupport.stopQuietly(turtle);
                }
            }, 100, 100, TimeUnit.MILLISECONDS);
            System.out.println("Bridge ready on " + topic + ". Press Ctrl+C to exit.");
            exit.await();
        } finally {
            watchdog.shutdownNow();
            ExampleSupport.stopQuietly(turtle);
            turtle.close();
        }
    }
}
