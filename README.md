# Getting Started with Turtle4J

![Java 21+](https://img.shields.io/badge/Java-21%2B-blue)
![Version 1.1.0](https://img.shields.io/badge/version-1.1.0-green)
[![Java CI](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml/badge.svg)](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml)

**Turtle4J** is a Java library for communicating with ROS 2 through rosbridge WebSockets. Use this repository to learn topic discovery, subscriptions, and publishing with runnable Java examples and a Docker-based Turtlesim environment.

![TURTLE4J architecture overview](https://github.com/user-attachments/assets/5a4f43a3-5cac-453b-af49-83aa24428003)

> [!NOTE]
> **Development status:** TURTLE4J 1.1.0 provides a Java client for communicating with ROS 2 through rosbridge and optional independent MQTT publish/subscribe. The Getting Started repository includes runnable Java examples and a Docker environment with ROS 2 Humble and Turtlesim 2D. The examples demonstrate connection, discovery, pose monitoring, and basic motion control. The Docker environment does not simulate a full TurtleBot 4, and successful execution in Turtlesim does not establish compatibility with or validation on physical TurtleBot 4 hardware.

## 0. Requirements

- **Java 21+** and Maven
- A ROS 2 system accessible through rosbridge (choose Option A or B below)

## 1. Choose your ROS 2 environment

Choose where your ROS 2 system will run:

- **Option A — Turtlesim 2D in Docker (recommended for beginners):** Run ROS 2 Humble, rosbridge, and a 2D turtle simulator locally without a physical robot. Follow the [Docker setup tutorial](docker/README.md). Java applications connect to `ws://localhost:9090`.
- **Option B — Physical TurtleBot 4:** Connect to a ROS 2 computer running rosbridge on your robot's network. Follow the [physical robot setup guide](docs/turtlebot4-setup.md). Java applications connect to `ws://<ROSBRIDGE_HOST_IP>:9090`.

Both environments use the same Turtle4J Java API. The Docker simulator is a learning environment, not a full TurtleBot 4 simulator.

## 2. Understand ROS 2

If you are new to ROS 2, complete [Understanding ROS 2: Nodes, Topics, and Messages](docs/ros2-basics.md). You will explore nodes and topics, observe turtle position messages, and send movement commands from a terminal.

If you already know ROS 2, you can proceed directly to the Java tutorial.

## 3. Using Turtle4J in your project

Follow the [Java Getting Started guide](docs/getting-started.md) to run your first example and connect Java to ROS 2 through rosbridge. The guide contains the commands, expected results, and setup details.

Turtle4J 1.1.0 is available on Maven Central. To use the library in your own Maven project, add:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

The runnable examples in this repository already include the dependency.

## 4. Examples

Ten ROS 2 examples cover connection, discovery, pose monitoring, velocity control, keyboard teleoperation, and feedback-based motion. See the [Java examples guide](docs/examples.md). MQTT examples are introduced separately after the ROS 2 exercises.

## 5. API documentation

- [Turtle4J 1.1.0 JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/) — published release
- [Development JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — evolving API documentation

## 6. Additional guides

- [Student Quick Start](docs/student-quick-start.md) — simulator-first onboarding and independent experiments
- [Docker simulator setup](docker/README.md)
- [Understanding ROS 2: Nodes, Topics, and Messages](docs/ros2-basics.md)
- [Java Getting Started](docs/getting-started.md)
- [Physical TurtleBot 4 setup](docs/turtlebot4-setup.md)
- [ROS 2 runtime discovery](docs/ros2-discovery.md)
- [API overview](docs/api-reference.md)

## Troubleshooting

- **Connection refused:** Confirm the ROS 2 system and rosbridge are running and port `9090` is reachable.
- **Wrong host:** For a remote ROS 2 system, `localhost` refers to the computer running Java, not the robot.
- **No pose or scan messages:** Verify the topic exists and the simulator or robot publishes that topic.
- **Build failure:** Check `mvn -version` for Java 21 and Maven dependency access.

