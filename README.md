# Getting Started with Turtle4J

![Java 21+](https://img.shields.io/badge/Java-21%2B-blue)
![Version 1.1.0](https://img.shields.io/badge/version-1.1.0-green)
[![Java Examples CI](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml/badge.svg)](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml)

**Turtle4J** is a Java library for communicating with ROS 2 through rosbridge WebSockets. Use this repository to learn topic discovery, subscriptions, and publishing with runnable Java examples and a Docker-based Turtlesim environment.

![TURTLE4J architecture overview](https://github.com/user-attachments/assets/5a4f43a3-5cac-453b-af49-83aa24428003)

> [!NOTE]
> **Development status:** TURTLE4J 1.1.0 provides a Java client for communicating with ROS 2 through rosbridge and optional independent MQTT publish/subscribe. The Getting Started repository includes runnable Java examples and a Docker environment with ROS 2 Humble and Turtlesim 2D. The examples demonstrate connection, discovery, pose monitoring, and basic motion control. The Docker environment does not simulate a full TurtleBot 4, and successful execution in Turtlesim does not establish compatibility with or validation on physical TurtleBot 4 hardware.

## Requirements

- **Java 21+** and Maven
- A ROS 2 system accessible through rosbridge (choose Option A or B below)

## Quick start

### 1. Choose your ROS 2 environment

**Option A — Turtlesim 2D in Docker:** Run the included ROS 2 Humble, rosbridge, and Turtlesim desktop locally without a physical robot. Connect to `ws://localhost:9090`. [Setup instructions](docs/getting-started.md#option-a-turtlesim-in-docker).

**Option B — Physical TurtleBot 4:** Connect through a reachable ROS 2 computer running rosbridge. Use `ws://<ROSBRIDGE_HOST_IP>:9090`. [Setup instructions](docs/getting-started.md#option-b-physical-turtlebot-4).

Both options use the same Java API. For the complete configuration steps, see the **[Getting Started guide](docs/getting-started.md)**.

### 2. Run your first Java example

From the **repository root**, with rosbridge running:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

The example connects to rosbridge and prints the discovery status. To connect to a different ROS 2 system, supply the reachable rosbridge host:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot -Dexec.args=ws://ROBOT_IP:9090
```

For a remote robot, `localhost` means the computer running Java, not the robot. See the [physical TurtleBot 4 setup instructions](docs/getting-started.md).

### 3. Explore more examples

Ten ROS 2 examples cover connection, discovery, pose monitoring, velocity control, keyboard teleoperation, and feedback-based motion. See the [Java examples guide](docs/examples.md). MQTT is introduced separately after the ROS 2 exercises.

## Install with Maven

TURTLE4J 1.1.0 is published to Maven Central. The examples in this repository already declare:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Examples

Ten ROS 2 examples cover connection, discovery, pose monitoring, velocity control, keyboard teleoperation, and feedback-based motion. See the [Java examples guide](docs/examples.md). MQTT examples are introduced separately after the ROS 2 exercises.

## API documentation

- [Turtle4J 1.1.0 JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/) — published release
- [Development JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — evolving API documentation

## Additional guides

- [Student Quick Start](docs/student-quick-start.md) — simulator-first onboarding and independent experiments
- [Docker simulator setup](docker/README.md)
- [ROS 2 and TurtleBot 4 setup](docs/getting-started.md)
- [ROS 2 runtime discovery](docs/ros2-discovery.md)
- [API overview](docs/api-reference.md)

## Troubleshooting

- **Connection refused:** Confirm the ROS 2 system and rosbridge are running and port `9090` is reachable.
- **Wrong host:** For a remote ROS 2 system, `localhost` refers to the computer running Java, not the robot.
- **No pose or scan messages:** Verify the topic exists and the simulator or robot publishes that topic.
- **Build failure:** Check `mvn -version` for Java 21 and Maven dependency access.

## Robot safety

Turtlesim is a 2D teaching simulator, not a full TurtleBot 4 simulation. Do not run motion examples unchanged on physical hardware. Follow the [ROS 2 and TurtleBot 4 setup guide](docs/getting-started.md) for hardware-specific precautions.
