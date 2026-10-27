package examples;

import com.javiergs.turtle4j.TurtleClient;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Keyboard teleoperation for a locally running Turtlesim instance.
 * Key release, focus loss, and window close request zero velocity.
 * This example is not a safety-rated controller for physical robots.
 *
 * @author javiergs
 * @version 1.0 (2026-10-26)
 */
public class KeyboardControl {
    private static final double LINEAR = 1.0;
    private static final double ANGULAR = 1.0;

    public static void main(String[] args) throws Exception {
        TurtleClient client = new TurtleClient(ExampleSupport.ENDPOINT);
        try {
            if (!ExampleSupport.requireVelocity(client.connect())) {
                client.close();
                return;
            }
            javax.swing.SwingUtilities.invokeAndWait(() -> openWindow(client));
        } catch (Exception ex) {
            client.close();
            throw ex;
        }
    }

    private static void openWindow(TurtleClient client) {
        JFrame frame = new JFrame("TURTLE4J — Keyboard Control");
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><center>↑ Forward &nbsp; ↓ Backward<br>"
                + "← Left &nbsp; → Right<br>Space: Stop<br><br>"
                + "Release keys or switch windows to stop.</center></html>", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 18f));
        panel.add(label, BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.setSize(440, 230);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Set<String> held = new HashSet<>();
        String[] keys = {"UP", "DOWN", "LEFT", "RIGHT"};
        for (String key : keys) {
            panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("pressed " + key), "press-" + key);
            panel.getActionMap().put("press-" + key, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent event) { held.add(key); }
            });
            panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("released " + key), "release-" + key);
            panel.getActionMap().put("release-" + key, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent event) { held.remove(key); }
            });
        }
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("pressed SPACE"), "stop");
        panel.getActionMap().put("stop", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) {
                held.clear();
                ExampleSupport.stopQuietly(client);
            }
        });

        // Re-publish commands while held. A robot-side watchdog is still needed
        // for safe operation if the network disconnects.
        Timer timer = new Timer(100, event -> {
            double linear = (held.contains("UP") ? LINEAR : 0.0)
                    - (held.contains("DOWN") ? LINEAR : 0.0);
            double angular = (held.contains("LEFT") ? ANGULAR : 0.0)
                    - (held.contains("RIGHT") ? ANGULAR : 0.0);
            try {
                client.setVelocity(linear, angular);
            } catch (RuntimeException ex) {
                ((Timer) event.getSource()).stop();
                held.clear();
                ExampleSupport.stopQuietly(client);
                label.setText("Velocity command failed: " + ex.getMessage());
            }
        });
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override public void windowLostFocus(WindowEvent event) {
                held.clear();
                ExampleSupport.stopQuietly(client);
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent event) {
                timer.stop();
                held.clear();
                ExampleSupport.stopQuietly(client);
                client.close();
            }
        });
        timer.start();
        frame.setVisible(true);
    }
}
