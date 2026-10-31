# MQTT to ROS 2 teleoperation (Turtlesim only)

This example migrates the useful idea from an older MQTT–ROS prototype into Turtle4J Getting Started. It demonstrates **one-way** command forwarding:

```text
Keyboard Java example -> local MQTT broker -> MQTT-to-ROS bridge -> Turtle4J -> rosbridge -> Turtlesim
```

It is **not** a bidirectional bridge or a physical TurtleBot 4 controller.

## Requirements

- Java 21 and Maven.
- The [Docker Turtlesim environment](../docker/README.md), with rosbridge at `ws://localhost:9090`.
- A **local** MQTT broker listening on `tcp://localhost:1883`. Start your own broker separately; the Docker Turtlesim setup does not provide one.

From the repository root, open two terminals:

**Terminal 1 — bridge:**

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.MqttToRosTeleop
```

**Terminal 2 — keyboard publisher:**

```bash
mvn exec:java -Dexec.mainClass=examples.MqttKeyboardTeleop
```

Enter `w`, `a`, `s`, or `d` and press Enter to send a velocity command. Enter `x` to stop, or `q` to stop and exit. The bridge expects payloads in the form `linear,angular` on `turtle4j/demo/cmd_vel`.

The bridge accepts only finite velocity components within [-1, 1] and requests a stop if no new command arrives for 500 ms. This is a **Java-side demonstration timeout, not a robot-side safety watchdog**. Network loss can prevent stop commands from reaching the robot. The default broker is local to avoid accepting commands from unrelated users on a public broker.

The bridge optionally accepts `rosbridge broker topic` as command-line arguments; the keyboard example accepts `broker topic`. Both programs must use the same MQTT topic.

## Why this example is separate

The normal Turtle4J examples talk to ROS 2 directly. This demonstration adds MQTT as an application-level integration layer without changing the Turtle4J library API. For basic motion control, start with [MoveTurtle](../src/main/java/examples/MoveTurtle.java) or [KeyboardControl](../src/main/java/examples/KeyboardControl.java).

**Safety:** Run this example only in Turtlesim. It has not been validated on a physical robot.
