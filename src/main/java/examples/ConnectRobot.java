package examples;

import com.javiergs.turtle4j.TurtleClient;

/**
 * Demonstrates connecting to a running rosbridge WebSocket endpoint.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class ConnectRobot {

    public static void main(String[] args) throws Exception {
        String endpoint = args.length > 0 ? args[0] : "ws://localhost:9090";
        TurtleClient client = new TurtleClient(endpoint);
        try {
            client.connect();
            System.out.println("Connected to " + endpoint);
            System.out.println("Discovery status: " + client.getCapabilities().getDiscoveryStatus());
        } finally {
            client.close();
        }
    }
}
