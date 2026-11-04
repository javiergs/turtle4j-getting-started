# TurtleBridge — MQTT ↔ ROS 2 with Turtlesim

**Why a bridge?** Turtle4J can publish and subscribe using MQTT, but MQTT broker topics are not ROS 2 topics. A message sent to an MQTT broker does not reach a ROS 2 node by itself. TurtleBridge forwards messages between these systems using rosbridge: for example, an MQTT velocity command reaches ROS 2 `/turtle1/cmd_vel`, and ROS 2 pose telemetry can be forwarded back to MQTT subscribers.

TurtleBridge is a bridge between an **MQTT broker** and **ROS 2**. It runs automatically in the same [Turtlesim Docker environment](../docker/README.md) used for direct ROS 2 and Turtle4J lessons. You do **not** need another Docker image or a separate TurtleBridge installation.

## 1. Start the environment

Complete the [Docker setup tutorial](../docker/README.md), open the browser desktop, and launch **Turtlesim 2D**. Keep the container running. Open a **second terminal on your own computer** for the following commands.

Find the running container:

```bash
docker ps
```

Copy the **CONTAINER ID** from the row whose image is `ghcr.io/javiergs/turtlesim-docker-environment:latest`. In the commands below, replace `<container-id>` with your actual ID (without angle brackets).

Check that both communication services are running:

```bash
docker exec <container-id> supervisorctl status
```

Look for `rosbridge RUNNING` and `turtlebridge RUNNING`. The desktop services should also be running.

## 2. Find your MQTT configuration

Each new container generates a configuration file. Read yours:

```bash
docker exec <container-id> cat /root/.turtlebridge/config.json
```

Example (your UUID will be different):

```json
{
  "rosbridgeUrl": "ws://localhost:9090",
  "mqttBroker": "tcp://broker.hivemq.com:1883",
  "mqttPrefix": "turtle4j/sim/12345678-1234-1234-1234-123456789abc",
  "commandTimeoutMs": 750,
  "maxLinearSpeed": 2,
  "maxAngularSpeed": 2,
  "topics": [
    {"ros": "/turtle1/cmd_vel", "type": "geometry_msgs/msg/Twist", "direction": "mqtt_to_ros"},
    {"ros": "/turtle1/pose", "type": "turtlesim/msg/Pose", "direction": "ros_to_mqtt"}
  ]
}
```

| Setting | Meaning |
| --- | --- |
| `rosbridgeUrl` | ROS 2 WebSocket connection **inside** the container. |
| `mqttBroker` | MQTT server; the default HiveMQ broker is public. |
| `mqttPrefix` | Unique namespace assigned to this container. |
| `commandTimeoutMs` | Stop the turtle if commands stop arriving for 750 milliseconds. |
| `maxLinearSpeed` / `maxAngularSpeed` | Configured speed limits for commands. |
| `topics` | ROS 2 topic names, message types, and forwarding directions. |

**Never copy the UUID from this example.** Copy your own `mqttPrefix` value from the configuration. The prefix can change when a new container is created. The UUID helps avoid accidental collisions between students; it does **not** provide access control or authentication.

## 3. Understand MQTT topics

A topic is an address used by MQTT publishers and subscribers. TurtleBridge forms its MQTT topic by appending the ROS 2 topic name **without its leading slash** to `mqttPrefix`:

```text
<mqttPrefix>/turtle1/cmd_vel
<mqttPrefix>/turtle1/pose
```

For the example UUID above, the command topic would be:

```text
turtle4j/sim/12345678-1234-1234-1234-123456789abc/turtle1/cmd_vel
```

| Part | Meaning |
| --- | --- |
| `turtle4j` | Project namespace. |
| `sim` | Simulation environment. |
| `12345678-...` | Random container identifier (UUID). |
| `turtle1` | Name of the simulated turtle. |
| `cmd_vel` | Velocity **commands** sent to the turtle. |
| `pose` | Position/orientation **telemetry** sent from the turtle. |

The direction matters: a student **publishes** to `cmd_vel` to move the turtle and **subscribes** to `pose` to observe it.

## 4. Understand the velocity message

The ROS 2 topic `/turtle1/cmd_vel` uses `geometry_msgs/msg/Twist`. TurtleBridge expects a JSON object with **two complete three-dimensional vectors**:

```json
{
  "linear":  {"x": 2.0, "y": 0.0, "z": 0.0},
  "angular": {"x": 0.0, "y": 0.0, "z": 1.0}
}
```

| Field | Meaning in Turtlesim |
| --- | --- |
| `linear.x` | Forward/backward speed; positive moves forward. |
| `linear.y` | Sideways velocity; keep `0.0` for Turtlesim. |
| `linear.z` | Vertical velocity; keep `0.0` for Turtlesim. |
| `angular.x` | Rotation about the X axis; keep `0.0`. |
| `angular.y` | Rotation about the Y axis; keep `0.0`. |
| `angular.z` | Turning speed; positive rotates counterclockwise. |

Turtlesim is 2D, but the standard ROS 2 `Twist` message describes 3D motion. **All six x/y/z fields must be included.** Omitting fields can produce the error `Expected x, y, z vector`.

## 5. Publish a command

Install the Mosquitto MQTT command-line clients on your **computer** (not inside Docker). For macOS with Homebrew:

```bash
brew install mosquitto
```

On other systems, install the Mosquitto clients using your operating system's package manager.

In the commands below, replace `YOUR_PREFIX` with the **entire** value of `mqttPrefix` from Step 2, for example `turtle4j/sim/12345678-1234-1234-1234-123456789abc`.

```bash
mosquitto_pub -h broker.hivemq.com -p 1883 \
  -t "YOUR_PREFIX/turtle1/cmd_vel" \
  -m '{"linear":{"x":2.0,"y":0.0,"z":0.0},"angular":{"x":0.0,"y":0.0,"z":1.0}}'
```

Watch the Turtlesim window. The turtle should move briefly while turning. `mosquitto_pub` publishes one message and exits.

**Why does it stop so quickly?** The bridge has a **750 ms command watchdog**. If no fresh command arrives, it sends a stop command to the simulator. Continuous movement requires publishing commands repeatedly at an interval **shorter than 750 ms**; this is a teaching example, not a physical-robot safety mechanism.

## 6. Subscribe to turtle telemetry

In another terminal, subscribe to the pose topic using **your same prefix**:

```bash
mosquitto_sub -h broker.hivemq.com -p 1883 \
  -t "YOUR_PREFIX/turtle1/pose" -v
```

You should receive position/orientation messages while Turtlesim runs. Leave the subscriber open, send another command from Step 5, and watch the reported pose change. Press **Ctrl+C** to stop the subscriber.

## 7. How the messages travel

```text
Your MQTT publisher
    → public MQTT broker
    → TurtleBridge (inside Docker)
    → rosbridge → ROS 2 /turtle1/cmd_vel
    → Turtlesim

Turtlesim /turtle1/pose
    → ROS 2 → rosbridge
    → TurtleBridge (inside Docker)
    → public MQTT broker
    → your MQTT subscriber
```

You can still use the **same running container** for [direct Java/Turtle4J communication](getting-started.md) through `ws://localhost:9090`. MQTT is an alternative communication path, not a separate simulator.

## Troubleshooting and safety

- **No turtle movement:** Verify Turtlesim is open, TurtleBridge is `RUNNING`, and the command topic contains your own prefix followed by `/turtle1/cmd_vel`.
- **`Expected x, y, z vector`:** Include all three coordinates in both `linear` and `angular`.
- **Movement lasts less than a second:** Expected; the watchdog stops motion after 750 ms without a new command.
- **No telemetry:** Check `/turtle1/pose`, your prefix, and that the simulator is publishing.
- **MQTT connection fails:** Verify network access to `broker.hivemq.com:1883`. Campus or corporate networks may block port 1883.
- **Bridge problems:** Use `docker logs <container-id>` to inspect its output.
- **Public broker:** Do not publish personal information, passwords, or research data. Random UUIDs are not security controls. Use this example only with Turtlesim, **not physical robots**.

**Native TurtleBridge installers:** Separate macOS, Windows, and Linux installers are still being prepared and validated. They are not needed for this Docker-based tutorial.
