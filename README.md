# Getting Started with Turtle4J

![Java 21+](https://img.shields.io/badge/Java-21%2B-blue)
![Version 1.1.0](https://img.shields.io/badge/version-1.1.0-green)
[![Java CI](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml/badge.svg)](https://github.com/javiergs/turtle4j-getting-started/actions/workflows/java-ci.yml)

Turtle4J is a Java library for communicating with ROS 2 systems, including TurtleBot 4 robots, through rosbridge WebSockets and MQTT messaging.

<img width="80%" alt="685696458-5a4f43a3-5cac-453b-af49-83aa24428003" src="https://github.com/user-attachments/assets/7ffff468-7847-491a-85df-983a49607bb0" />

## Step 1. Choose your ROS 2 environment

Choose where your ROS 2 system will run:

### Option A — Turtlesim 2D in Docker (recommended for beginners)

Run one Docker image containing ROS 2 Humble, Turtlesim, rosbridge, and the MQTT ↔ ROS 2 bridge, without a physical robot. Follow the [Docker setup tutorial](docker/README.md).

**Rosbridge WebSocket address:**

```text
ws://localhost:9090
```

### Option B — Physical TurtleBot 4

Connect to a ROS 2 computer running rosbridge on your robot's network. Follow the [physical robot setup guide](docs/turtlebot4-setup.md).

**Rosbridge WebSocket address:**

```text
ws://<ROSBRIDGE_HOST_IP>:9090
```

## Step 2. Understand ROS 2

If you are new to ROS 2, complete [Understanding ROS 2: Nodes, Topics, and Messages](docs/ros2-basics.md). You will explore nodes and topics, observe turtle position messages, and send movement commands from a terminal.

## Step 3. Using Turtle4J in your project

To run the Java examples, install **Java 21 or newer** and **Maven** on your computer. You can verify both installations with `java -version` and `mvn -version`. These tools are not required for the Docker and ROS 2 tutorials above.

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

## TurtleBridge — MQTT ↔ ROS 2 application

[TurtleBridge MQTT tutorial](docs/turtlebridge.md) — use the **same Docker environment** to publish movement commands and receive pose telemetry over MQTT without writing Java.

## Examples

Ten ROS 2 examples cover connection, discovery, pose monitoring, velocity control, keyboard teleoperation, and feedback-based motion. See the [Java examples guide](docs/examples.md). MQTT examples are introduced separately after the ROS 2 exercises.

## API documentation

- [Turtle4J 1.1.0 JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/) — published release
- [Development JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — evolving API documentation

## Additional guides

- [Developing New Functionality with Turtle4J](docs/extending-turtle4j.md) — design, implement, and test independent Java experiments.
- [ROS 2 runtime discovery](docs/ros2-discovery.md) — inspect capabilities and troubleshoot discovery.
- [API overview](docs/api-reference.md) — learn the library's Java API.
- [MQTT publish/subscribe](docs/mqtt-example.md) — experiment with independent MQTT messaging.

## Troubleshooting

- **Connection refused:** Confirm the ROS 2 system and rosbridge are running and port `9090` is reachable.
- **Wrong host:** For a remote ROS 2 system, `localhost` refers to the computer running Java, not the robot.
- **No pose or scan messages:** Verify the topic exists and the simulator or robot publishes that topic.
- **Build failure:** Check `mvn -version` for Java 21 and Maven dependency access.

