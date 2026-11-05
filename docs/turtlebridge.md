# turtle4j-mqtt-bridge — MQTT ↔ ROS 2

**Why a bridge?** MQTT broker topics and ROS 2 topics are separate communication systems. **turtle4j-mqtt-bridge** forwards selected MQTT messages to ROS 2 through rosbridge and forwards selected ROS 2 messages back to MQTT subscribers.

## Choose your environment

| What you have | What you can try |
| --- | --- |
| **Docker + Turtlesim** | The bridge is **already installed**. Follow [Part A](#part-a--use-the-bridge-with-turtlesim) to send MQTT movement commands to the simulated turtle and receive pose telemetry. |
| **Physical TurtleBot 4** | Follow the [standalone installation guide](turtlebridge-installation.md) to check the executable on your Raspberry Pi, then follow [Part B](#part-b--explore-read-only-robot-telemetry-experimental) to discover topics and attempt a **read-only** MQTT telemetry experiment. |

**Current status (v0.1.0):** Turtlesim movement and pose forwarding are supported. The standalone JAR is built for Java 21; Raspberry Pi ARM64 runtime and physical TurtleBot 4 telemetry have **not yet been validated**. The bridge rejects physical-robot motion mappings. Do not attempt to drive a physical robot with this release.

## Part A — Use the bridge with Turtlesim

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

The direction matters: you **publish** to `cmd_vel` to move the turtle and **subscribes** to `pose` to observe it.

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

## Part B — Explore read-only robot telemetry (experimental)

This section is an **unverified experiment**, not a tested TurtleBot 4 integration. You can attempt to forward a robot's ROS 2 messages to MQTT using the existing generic `ros_to_mqtt` mapping. Do **not** configure any `mqtt_to_ros` topics for a physical robot.

### 1. Check the standalone executable

Follow the [standalone installation and verification guide](turtlebridge-installation.md). Verify the JAR checksum, Java runtime, and `--help` output before proceeding. Installing the JAR on the Raspberry Pi does not by itself connect it to ROS 2.

### 2. Discover available robot topics

On a trusted computer connected to the robot's ROS 2 network, with the correct ROS 2 environment sourced, run:

```bash
ros2 topic list
ros2 topic list -t
```

Look for an odometry topic (often `/odom`, but the actual name may be namespaced). If found, inspect its message type and sample data:

```bash
ros2 topic info /odom
ros2 topic echo /odom --once
```

Replace `/odom` with the actual discovered topic. Do not assume any particular topic is present.

### 3. Confirm rosbridge access

The bridge connects to a **rosbridge WebSocket server**, which may run on the robot or another ROS 2 computer. It is not guaranteed to be installed on TurtleBot 4. Follow the [physical TurtleBot 4 connection guide](turtlebot4-setup.md) to set it up on a trusted network. Record the reachable WebSocket address, such as `ws://ROSBRIDGE_HOST:9090`; do not expose rosbridge to the public Internet.

### 4. Prepare a read-only configuration

Create a separate configuration file on the computer where the standalone JAR will run. This example assumes the robot publishes `/odom` as `nav_msgs/msg/Odometry`—**verify both values first**:

```json
{
  "rosbridgeUrl": "ws://ROSBRIDGE_HOST:9090",
  "mqttBroker": "tcp://broker.hivemq.com:1883",
  "mqttPrefix": "turtle4j/robot/REPLACE_WITH_UNIQUE_ID",
  "commandTimeoutMs": 750,
  "maxLinearSpeed": 1,
  "maxAngularSpeed": 1,
  "topics": [
    {
      "ros": "/odom",
      "type": "nav_msgs/msg/Odometry",
      "direction": "ros_to_mqtt"
    }
  ]
}
```

Replace `ROSBRIDGE_HOST` and `REPLACE_WITH_UNIQUE_ID` with your own values; the prefix must be unique. This example uses a **public MQTT broker**, so do not forward sensitive telemetry, identifying data, or private network information. Prefer a private, access-controlled broker for actual robot deployments. The speed settings are required by v0.1.0 configuration validation but do not enable motion without an `mqtt_to_ros` mapping.

### 5. Start the bridge and observe MQTT

Save the JSON above as `robot-readonly.json` in the same directory as the downloaded JAR. From that directory, run:

```bash
java -jar turtle4j-mqtt-bridge-0.1.0.jar --config robot-readonly.json
```

In another terminal with Mosquitto clients installed, subscribe to the matching MQTT topic:

```bash
mosquitto_sub -h broker.hivemq.com -p 1883 \
  -t "turtle4j/robot/REPLACE_WITH_UNIQUE_ID/odom" -v
```

If the robot's topic has a namespace, the MQTT suffix follows the discovered ROS 2 topic name without its initial slash. Successful output would demonstrate **read-only forwarding** of that topic, not general TurtleBot 4 support. If no messages arrive, verify the ROS topic name and type, rosbridge reachability, and the bridge's console output. Stop with **Ctrl+C**.

### 6. Report the results

Record the Raspberry Pi architecture and operating system, Java version, discovered ROS topic and type, rosbridge host location (without credentials), bridge startup messages, and whether MQTT telemetry arrived. Do not attempt motion control; report errors rather than bypassing the restrictions.

## Troubleshooting and safety

- **No turtle movement:** Verify Turtlesim is open, TurtleBridge is `RUNNING`, and the command topic contains your own prefix followed by `/turtle1/cmd_vel`.
- **`Expected x, y, z vector`:** Include all three coordinates in both `linear` and `angular`.
- **Movement lasts less than a second:** Expected; the watchdog stops motion after 750 ms without a new command.
- **No telemetry:** Check `/turtle1/pose`, your prefix, and that the simulator is publishing.
- **MQTT connection fails:** Verify network access to `broker.hivemq.com:1883`. Campus or corporate networks may block port 1883.
- **Bridge problems:** Use `docker logs <container-id>` to inspect its output.
- **Public broker:** Do not publish personal information, passwords, or research data. Random UUIDs are not security controls. Use this example only with Turtlesim, **not physical robots**.

**Native TurtleBridge installers:** Separate macOS, Windows, and Linux installers are still being prepared and validated. They are not needed for this Docker-based tutorial.
