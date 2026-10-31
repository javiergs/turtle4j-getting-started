package examples;

import com.javiergs.turtle4j.TurtleClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Sends simple keyboard velocity commands to the companion MQTT-to-ROS bridge.
 * Run only with a local broker and Turtlesim.
 *
 * @author javiergs
 * @version 1.0 (2026-10-31)
 */
public final class MqttKeyboardTeleop {
    private MqttKeyboardTeleop() { }

    public static void main(String[] args) throws Exception {
        String broker = args.length > 0 ? args[0] : "tcp://localhost:1883";
        String topic = args.length > 1 ? args[1] : "turtle4j/demo/cmd_vel";
        TurtleClient turtle = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            turtle.connectMqtt(broker);
            System.out.println("Enter w/a/s/d, x to stop, or q to quit (then Enter).");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = input.readLine()) != null) {
                String command = switch (line.trim().toLowerCase()) {
                    case "w" -> "1.0,0.0";
                    case "s" -> "-1.0,0.0";
                    case "a" -> "0.0,1.0";
                    case "d" -> "0.0,-1.0";
                    case "x", "q" -> "0.0,0.0";
                    default -> null;
                };
                if (command == null) {
                    System.out.println("Use w/a/s/d, x, or q.");
                    continue;
                }
                turtle.publishMqtt(topic, command);
                if ("q".equalsIgnoreCase(line.trim())) break;
            }
        } finally {
            try { turtle.publishMqtt(topic, "0.0,0.0"); }
            catch (RuntimeException ignored) { }
            turtle.close();
        }
    }
}
