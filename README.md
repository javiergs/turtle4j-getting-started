# Getting Started

Turtle4J is a Java 21 library for communicating with ROS 2 systems through a rosbridge WebSocket connection. It supports topic publishing, subscriptions, and runtime discovery of advertised ROS 2 interfaces.

This repository provides four complementary resources: a **Docker-based simulator** for experimenting without physical hardware, an introduction to **ROS 2 fundamentals**, the **turtle4j Java library** for communicating with ROS 2 systems, and **turtle4j-mqtt-bridge** for connecting MQTT applications to ROS 2.

<img width="1024" height="683" alt="52FFEAEF-2B1E-4823-935F-09867AA85373" src="https://github.com/user-attachments/assets/5da71059-2a81-4381-b8d0-fbd3ca0cbdc1" />

## 1. Need a ROS 2 simulator?

The **Turtlesim Docker Environment** provides Ubuntu 22.04, ROS 2 Humble, Turtlesim 2D, rosbridge, a browser-accessible Linux desktop, and turtle4j-mqtt-bridge for MQTT communication. You do not need a physical robot or a separate ROS 2 installation.

**Docker package:**

```text
ghcr.io/javiergs/turtlesim-docker-environment:latest
```

**[Set up the Docker simulator](docker/README.md)**

Turtlesim is a simple 2D teaching simulator, not a full TurtleBot 4 simulator. You can later connect to a physical ROS 2 robot instead.

## 2. Want to learn ROS 2?

Learn about **nodes, topics, and messages**, and practice ROS 2 terminal commands for discovering, publishing, and subscribing to topics.

**[Learn ROS 2 fundamentals](docs/ros2-basics.md)**

## 3. Want to develop Java applications?

**turtle4j** is a Java library for communicating with ROS 2 systems through **rosbridge WebSockets** or **MQTT**. MQTT communication with ROS 2 requires an MQTT-to-ROS 2 bridge, such as **turtle4j-mqtt-bridge**. Start by connecting to Turtlesim, then adapt your application to the topics and capabilities of a physical robot such as TurtleBot 4.

**Maven dependency:**

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

**[Get started with the Turtle4J Java library](docs/getting-started.md)**

For runnable code, see the [Java examples](docs/examples.md), [API overview](docs/api-reference.md), and [Turtle4J 1.1.0 JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/).

## 4. Need MQTT communication with ROS 2?

**turtle4j** supports MQTT publishing and subscribing, but ROS 2 nodes cannot directly receive messages from an MQTT broker. **turtle4j-mqtt-bridge** connects these systems through rosbridge, forwarding MQTT commands to ROS 2 topics and publishing ROS 2 telemetry to MQTT subscribers.

**[Learn about turtle4j-mqtt-bridge and MQTT](docs/turtlebridge.md)**

The bridge is included in the [Turtlesim Docker environment](docker/README.md), so no additional installation is required when using the simulator.

It is also available as a [standalone Java 21 executable (v0.1.0)](https://github.com/javiergs/turtle4j-getting-started/releases/tag/bridge-v0.1.0). The standalone application is designed to run on Java 21-compatible systems, including Linux ARM64 devices such as the Raspberry Pi used by TurtleBot 4. You can follow the [standalone installation and verification guide](docs/turtlebridge.md#8-standalone-jar-installation) to test the executable on your device.

**Current status:** Version 0.1.0 supports Turtlesim. Raspberry Pi ARM64 compatibility is pending validation, and physical TurtleBot 4 communication is not yet supported.

## Additional resources

- [Physical TurtleBot 4 setup](docs/turtlebot4-setup.md)
- [ROS 2 runtime discovery](docs/ros2-discovery.md)
- [Developing new functionality with Turtle4J](docs/extending-turtle4j.md)
- [MQTT publish/subscribe examples](docs/mqtt-example.md)
