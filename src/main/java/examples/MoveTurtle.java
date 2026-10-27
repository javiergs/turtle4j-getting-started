package examples;

import com.javiergs.turtle4j.TurtleClient;

/**
 * Move forward briefly in Turtlesim using discovered velocity control.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class MoveTurtle {
    public static void main(String[] args) throws Exception {
        TurtleClient client = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            if (!ExampleSupport.requireVelocity(client.connect())) return;
            client.setVelocity(1.0, 0.0);
            Thread.sleep(1000);
            client.stop();
        } finally {
            ExampleSupport.stopQuietly(client);
            client.close();
        }
    }
}
