package examples;

import com.javiergs.turtle4j.TurtleClient;

/**
 * Rotate in place in Turtlesim using discovered velocity control.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class RotateTurtle {
    public static void main(String[] args) throws Exception {
        TurtleClient client = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            if (!ExampleSupport.requireVelocity(client.connect())) return;
            client.setVelocity(0.0, 1.0);
            Thread.sleep(1500);
            client.stop();
        } finally {
            ExampleSupport.stopQuietly(client);
            client.close();
        }
    }
}
