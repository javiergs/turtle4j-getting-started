# TURTLE4J Getting Started

Runnable examples and setup guides for **TURTLE4J**, a Java 21 client for ROS 2 through rosbridge.

## First example: connect to rosbridge

Requires Java 21+, Maven, and a running rosbridge server. The default endpoint is `ws://localhost:9090` (the optional Turtlesim Docker environment will be migrated separately).

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

To use a different endpoint, pass it as an argument:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot -Dexec.args=ws://ROBOT_IP:9090
```

This project depends on the **published Maven Central artifact** `com.javiergs:turtle4j:1.0.0`; it does not compile the private library's source code. A reachable rosbridge endpoint is still required.

## Environments

TURTLE4J connects Java applications to ROS 2 through rosbridge. Choose either a local **Turtlesim 2D** Docker environment or a **physical TurtleBot 4** running ROS 2. Turtlesim is not a full TurtleBot 4 simulator.

![TURTLE4J overview](https://github.com/user-attachments/assets/5a4f43a3-5cac-453b-af49-83aa24428003)

- [Getting started: Docker or physical TurtleBot 4](docs/getting-started.md)
- [Turtlesim example guide](docs/examples.md)
- [ROS 2 discovery](docs/ros2-discovery.md)
- [API overview](docs/api-reference.md)

## Runnable examples

The project includes ten progressive examples, from connection and discovery to velocity control, pose feedback, and keyboard teleoperation. Each uses the published Maven Central library, not the private source tree.

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.DiscoverRobot
```

The motion examples target Turtlesim 2D and **must not be run unchanged on a physical TurtleBot 4**. See the [example guide](docs/examples.md) for details.

## API documentation

- [TURTLE4J 1.0.0 Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.0.0/) — frozen release reference.
- [Development Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — automatically refreshed through reviewed pull requests.

See the [documentation maintenance guide](docs/javadoc/README.md) for details.
