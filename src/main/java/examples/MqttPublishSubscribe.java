package examples;

import com.javiergs.turtle4j.TurtleClient;

/**
 * Demonstrates optional MQTT publishing and subscribing alongside rosbridge.
 * Requires a TURTLE4J build that includes the proposed MQTT API.
 */
public class MqttPublishSubscribe {
    public static void main(String[] args) throws Exception {
        String rosbridge = args.length > 0 ? args[0] : "ws://localhost:9090";
        String broker = args.length > 1 ? args[1] : "tcp://broker.hivemq.com:1883";

        TurtleClient turtle = new TurtleClient(rosbridge);
        try {
            turtle.connect();

            // Optional MQTT connection
            turtle.connectMqtt(broker);

            // Publish to MQTT
            turtle.publishMqtt("robot/status", "Ready");

            // Subscribe to MQTT
            turtle.subscribeMqtt("robot/commands", (topic, message) -> {
                System.out.println(message);
            });

            // Existing TURTLE4J functionality (only on a verified robot/topic)
            // turtle.setVelocity(0.2, 0.0);

            // Keep the subscription active briefly for demonstration.
            Thread.sleep(10_000);
        } finally {
            turtle.close();
        }
    }
}
