# Install turtle4j-mqtt-bridge

This guide explains how to download and verify the standalone executable. For MQTT topics, message formats, and Turtlesim examples, see [Using turtle4j-mqtt-bridge](turtlebridge.md).

The [v0.1.0 release](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0) requires **Java 21**. Requirements may change with future releases; check the release notes for your version.

## Install and verify the standalone JAR

The [turtle4j-mqtt-bridge v0.1.0 release](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0) provides a standalone executable JAR. The standalone v0.1.0 JAR ran directly on a physical TurtleBot 4 Raspberry Pi (ARM64 `aarch64`, Ubuntu 24.04.4 LTS, OpenJDK 21, ROS 2 Jazzy). It forwarded `/battery_state` (`sensor_msgs/msg/BatteryState`) through local rosbridge to a remote MQTT subscriber. Odometry, LiDAR, and IMU forwarding remain unvalidated. The hardware test also recorded a successful checksum verification.

**Physical robot safety.** Use only `ros_to_mqtt` mappings on physical robots. Physical-robot `mqtt_to_ros` mappings are prohibited. The v0.1.0 command path is limited to simulated Turtlesim.

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

After verifying the checksum and JAR startup, follow the [read-only robot telemetry procedure](turtlebridge.md#part-b--explore-read-only-robot-telemetry-experimental). Start the JAR with `--config robot-readonly.json` and include only `ros_to_mqtt` mappings. The default configuration includes a simulated Turtlesim command mapping.
