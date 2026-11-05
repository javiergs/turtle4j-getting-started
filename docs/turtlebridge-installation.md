# Install turtle4j-mqtt-bridge

This guide explains how to download and verify the standalone executable. For MQTT topics, message formats, and Turtlesim examples, see [Using turtle4j-mqtt-bridge](turtlebridge.md).

The [v0.1.0 release](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0) requires **Java 21**. Requirements may change with future releases; check the release notes for your version.

## Install and verify the standalone JAR

The [turtle4j-mqtt-bridge v0.1.0 release](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0) provides a standalone executable JAR. It is **built with Java 21** and is intended to be usable on Linux ARM64 computers, including the Raspberry Pi used by TurtleBot 4. **ARM64 operation has not yet been tested on a physical TurtleBot 4**; this is an installation validation exercise, not a compatibility certification.

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

Record the operating system, Java version, whether checksum verification succeeded, the complete startup result, and whether rosbridge is available. Do not include passwords, tokens, or private network credentials in the report.

**What this release does not establish:** Passing the steps above demonstrates that the JAR starts on your Raspberry Pi. It does not demonstrate that the current Turtlesim-only bridge supports TurtleBot 4 ROS 2 topics, telemetry, or motion. Those require a separately reviewed implementation and supervised tests.

