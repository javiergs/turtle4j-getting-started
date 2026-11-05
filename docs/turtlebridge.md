# turtle4j-mqtt-bridge — MQTT ↔ ROS 2

**Why a bridge?** MQTT broker topics and ROS 2 topics are separate communication systems. **turtle4j-mqtt-bridge** forwards selected MQTT messages to ROS 2 through rosbridge and forwards selected ROS 2 messages back to MQTT subscribers.

This guide covers two ways to use the bridge:

1. **Turtlesim with Docker:** Use the bridge already included in the [Turtlesim Docker environment](../docker/README.md) to send simulated movement commands and receive pose telemetry.
2. **Standalone Java 21 executable:** Download and check the [v0.1.0 JAR](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0), including a startup check on a Raspberry Pi ARM64 device.

**Current limitations:** Version 0.1.0 supports Turtlesim topics only. Running the JAR on a TurtleBot 4 Raspberry Pi is an installation/compatibility test, **not** physical robot integration. Physical TurtleBot 4 communication and motion are not supported by this release.

## Part A — Use the bridge with Turtlesim (Docker)

The bridge starts automatically in the Docker environment. No separate JAR installation is required for this part.

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

## Part B — Install the standalone bridge (Java 21)

## 8. Standalone JAR installation (Java 21)

The [turtle4j-mqtt-bridge v0.1.0 release](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0) provides a standalone executable JAR. It is **built with Java 21** and is intended to be usable on Linux ARM64 computers, including the Raspberry Pi used by TurtleBot 4. **ARM64 operation has not yet been tested on a physical TurtleBot 4**; this is a student validation exercise, not a compatibility certification.

**Important:** v0.1.0 supports the **Turtlesim** topics `/turtle1/cmd_vel` and `/turtle1/pose`. It is **not** a physical TurtleBot 4 control application. Do not configure it to drive the robot, and do not forward velocity commands to physical robot topics.

### A. Inspect the Raspberry Pi (do this first)

Connect to the Raspberry Pi over SSH using the hostname and account supplied by your team. On **the Pi**, run:

```bash
uname -m
cat /etc/os-release
java -version
```

Record the full output. `aarch64` indicates 64-bit ARM Linux. If Java is missing or its major version is not 21, **stop and report the results** before installing software: the correct installation procedure depends on the operating system and configured repositories.

### B. Download the binary (after Java 21 is available)

On the Raspberry Pi, create a working directory and download the public release:

```bash
mkdir -p ~/turtle4j-mqtt-bridge
cd ~/turtle4j-mqtt-bridge
curl -fL -O https://github.com/javiergs/turtle4j-getting-started/releases/download/bridge-v0.1.0/turtle4j-mqtt-bridge-0.1.0.jar
curl -fL -O https://github.com/javiergs/turtle4j-getting-started/releases/download/bridge-v0.1.0/SHA256SUMS
sha256sum -c SHA256SUMS
```

The checksum check must report `OK`. If it fails, do not run the JAR.

### C. Check that the JAR starts

```bash
java -jar turtle4j-mqtt-bridge-0.1.0.jar --help
```

The help text should show `Usage:`. This checks Java startup only; it does **not** establish a connection to ROS 2 or prove compatibility with TurtleBot 4.

### D. Inspect ROS 2 connectivity without moving the robot

Identify a **trusted ROS 2 host** with rosbridge running and reachable from the Pi. rosbridge is not necessarily installed on the Pi by default. The existing [physical TurtleBot 4 connection guide](turtlebot4-setup.md) explains how to set up rosbridge.

For this first validation, **do not run the bridge against physical robot topics**. Instead, record:

```bash
uname -m
java -version
java -jar ~/turtle4j-mqtt-bridge/turtle4j-mqtt-bridge-0.1.0.jar --help
```

Report the operating system, Java version, whether checksum verification succeeded, the complete startup result, and whether rosbridge is available. Do not include passwords, tokens, or private network credentials in the report.

**What this release does not establish:** Passing the steps above demonstrates that the JAR starts on your Raspberry Pi. It does not demonstrate that the current Turtlesim-only bridge supports TurtleBot 4 ROS 2 topics, telemetry, or motion. Those require a separately reviewed implementation and supervised tests.

## Troubleshooting and safety

- **No turtle movement:** Verify Turtlesim is open, TurtleBridge is `RUNNING`, and the command topic contains your own prefix followed by `/turtle1/cmd_vel`.
- **`Expected x, y, z vector`:** Include all three coordinates in both `linear` and `angular`.
- **Movement lasts less than a second:** Expected; the watchdog stops motion after 750 ms without a new command.
- **No telemetry:** Check `/turtle1/pose`, your prefix, and that the simulator is publishing.
- **MQTT connection fails:** Verify network access to `broker.hivemq.com:1883`. Campus or corporate networks may block port 1883.
- **Bridge problems:** Use `docker logs <container-id>` to inspect its output.
- **Public broker:** Do not publish personal information, passwords, or research data. Random UUIDs are not security controls. Use this example only with Turtlesim, **not physical robots**.

**Native TurtleBridge installers:** Separate macOS, Windows, and Linux installers are still being prepared and validated. They are not needed for this Docker-based tutorial.
