# TURTLE4J Getting Started

**TURTLE4J** is a Java library for communicating with ROS 2 robots through a rosbridge WebSocket connection. This repository provides runnable examples, a Docker-based Turtlesim environment, and setup guides for ROS 2 systems such as TurtleBot 4.

![TURTLE4J architecture overview](https://github.com/user-attachments/assets/5a4f43a3-5cac-453b-af49-83aa24428003)

## Getting started

### 1. Requirements

- Java 21 or newer
- Apache Maven
- A running ROS 2 system with rosbridge
- Docker (optional, for the included Turtlesim 2D environment)

### 2. Start Turtlesim 2D (optional)

The Docker environment includes ROS 2 Humble, rosbridge, Turtlesim 2D, and a browser-based desktop.

```bash
cd docker
docker compose up --build
```

Open [the local desktop](http://localhost:6080/vnc.html), click **Connect**, and launch **Turtlesim 2D**. The rosbridge WebSocket endpoint is `ws://localhost:9090`.

See the [Docker setup guide](docker/README.md) for details. Turtlesim 2D is **not** a full TurtleBot 4 simulator.

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

The [Javadoc maintenance guide](docs/javadoc/README.md) explains the documentation workflows.
