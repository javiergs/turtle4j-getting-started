# Runnable Java examples

The examples progress from connecting to ROS 2 through velocity control and pose-feedback navigation. They use the local **Docker Turtlesim 2D** environment, not a physical TurtleBot 4. See [Docker setup](../docker/README.md).

New students should follow the [Student Quick Start](student-quick-start.md) first.

## Run an example

Start Docker, open its browser desktop, and launch **Turtlesim 2D**. From the repository root:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

Replace `ConnectRobot` with any class name below. The endpoint is `ws://localhost:9090`.

## Progressive examples

| Level | Example | What it demonstrates | Requirements |
| --- | --- | --- | --- |
| 1 | [ConnectRobot](../src/main/java/examples/ConnectRobot.java) | Connect and report discovery status | rosbridge |
| 2 | [DiscoverRobot](../src/main/java/examples/DiscoverRobot.java) | Print discovered capabilities, topics and services | rosbridge; rosapi for complete discovery |
| 3 | [ReadPose](../src/main/java/examples/ReadPose.java) | Print Turtlesim pose for 10 seconds | Discovered `/turtle1/pose` |
| 4 | [MoveTurtle](../src/main/java/examples/MoveTurtle.java) | Move forward for 1 second and stop | Unambiguous VELOCITY |
| 5 | [RotateTurtle](../src/main/java/examples/RotateTurtle.java) | Rotate in place | Unambiguous VELOCITY |
| 6 | [CirclePath](../src/main/java/examples/CirclePath.java) | Combine linear and angular velocities | Unambiguous VELOCITY |
| 7 | [KeyboardControl](../src/main/java/examples/KeyboardControl.java) | Control with arrow keys in a Swing window | Unambiguous VELOCITY; graphical desktop |
| 8 | [SquarePath](../src/main/java/examples/SquarePath.java) | Use pose feedback to measure sides and turns | Unambiguous VELOCITY and Turtlesim pose |
| 9 | [MoveDistance](../src/main/java/examples/MoveDistance.java) | Move 1 meter using feedback | VELOCITY and Turtlesim pose |
| 10 | [RotateAngle](../src/main/java/examples/RotateAngle.java) | Rotate 90 degrees using feedback | VELOCITY and Turtlesim pose |

The examples use `TurtleClient` and check the capabilities they need before acting. If a required capability is unavailable, ambiguous, or discovery is unknown, the example prints a message and exits. `ReadPose` and `SquarePath` additionally require the Turtlesim-specific `turtlesim/msg/Pose` topic. This topic is checked explicitly among discovered extras because it is **not** the `nav_msgs/msg/Odometry` capability used by many physical robots.

## Keyboard controls

Run:

```bash
mvn exec:java -Dexec.mainClass=examples.KeyboardControl
```

Click the Swing control window and use **↑ / ↓** for forward/backward, **← / →** to rotate, and **Space** to stop. Releasing the keys or switching away from the window requests a stop. Closing the window also requests a stop and closes the connection. The control window appears on the **Java computer**, not inside the Docker noVNC desktop.

**Safety:** The examples are educational, open-loop velocity programs except `SquarePath`, `MoveDistance`, and `RotateAngle`, which use pose feedback. Sending zero velocity is not a safety guarantee. A disconnected robot may miss stop commands; physical robot teleoperation requires a robot-side command timeout/watchdog and additional safety measures. Do not run these examples unchanged on a physical TurtleBot 4.

See [ROS 2 discovery](ros2-discovery.md) and the [API reference](api-reference.md).

## Advanced helper: raw velocity publishing

[`VelocityCommands.java`](../src/main/java/examples/VelocityCommands.java) is a package-private utility for examples that need to publish `geometry_msgs/msg/Twist` directly through `RosbridgeConnection` to an explicitly chosen topic. It provides `move(connection, topic, linear, angular)` and `stop(connection, topic)`. It is not a runnable `main` class, and the progressive examples above use `TurtleClient` instead. Keep this helper in the examples project rather than the published API.

## Optional MQTT example

After completing the ROS 2 examples, see [MQTT publish/subscribe](mqtt-example.md). MQTT in TURTLE4J 1.1.0 is independent of ROSbridge; it does not automatically forward ROS messages.

For an application-level MQTT → ROS 2 demonstration using the existing Turtle4J API, see [MQTT to ROS 2 teleoperation](mqtt-ros-teleop.md). It uses a local broker and Turtlesim only; MQTT does not automatically bridge to ROS 2.
