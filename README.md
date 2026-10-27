# TURTLE4J Getting Started

**TURTLE4J** is a Java library for communicating with ROS 2 robots through a rosbridge WebSocket connection. This repository provides runnable examples, a Docker-based Turtlesim environment, and setup guides for ROS 2 systems such as TurtleBot 4.

![TURTLE4J architecture overview](https://github.com/user-attachments/assets/5a4f43a3-5cac-453b-af49-83aa24428003)

## Getting started

### 1. Requirements

- Java 21 or newer
- Apache Maven
- A ROS 2 system accessible through rosbridge (choose Option A or B below).

### 2. Choose your ROS 2 environment

**Option A — Turtlesim 2D in Docker:** Run the included ROS 2 Humble, rosbridge, and Turtlesim desktop locally without a physical robot. Connect to `ws://localhost:9090`. [Setup instructions](docs/getting-started.md#option-a-turtlesim-in-docker).

**Option B — Physical TurtleBot 4:** Connect through a reachable ROS 2 computer running rosbridge. Use `ws://<ROSBRIDGE_HOST_IP>:9090`. [Setup instructions](docs/getting-started.md#option-b-physical-turtlebot-4).

Both options use the same Java API. For the complete configuration steps, see the **[Getting Started guide](docs/getting-started.md)**.

### 3. Run your first Java example

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

### 4. Explore more examples

Ten progressive examples cover connection, discovery, pose monitoring, velocity control, keyboard teleoperation, and feedback-based motion. See the [Java examples guide](docs/examples.md).

**Safety:** The included motion examples target Turtlesim 2D. Do not run them unchanged on a physical TurtleBot 4.

## Maven dependency

TURTLE4J 1.0.0 is published to Maven Central. The examples in this repository already declare:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Documentation

- [Docker simulator setup](docker/README.md)
- [ROS 2 and TurtleBot 4 setup](docs/getting-started.md)
- [Java examples](docs/examples.md)
- [ROS 2 runtime discovery](docs/ros2-discovery.md)
- [API overview](docs/api-reference.md)
- [TURTLE4J 1.0.0 Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.0.0/) — frozen release documentation
- [Development Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — updated through reviewed pull requests

