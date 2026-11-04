# Getting Started with Turtle4J

Resources for learning ROS 2 and developing Java applications that communicate with simulated and physical robots.

This repository brings together four complementary resources. Start with a simulator if you do not have a robot, explore ROS 2 communication, and then build Java applications that can connect to a compatible ROS 2 system.

![Turtle4J architecture](https://github.com/user-attachments/assets/7ffff468-7847-491a-85df-983a49607bb0)

## 1. Need a ROS 2 simulator?

The **Turtlesim Docker Environment** provides Ubuntu 22.04, ROS 2 Humble, Turtlesim 2D, rosbridge, a browser-accessible Linux desktop, and TurtleBridge for MQTT communication. You do not need a physical robot or a separate ROS 2 installation.

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

**Turtle4J** is a Java library for communicating with ROS 2 systems through **rosbridge WebSockets** and **MQTT**. Start by connecting to Turtlesim, then adapt your application to the topics and capabilities of a physical robot such as TurtleBot 4.

**Maven dependency:** `com.javiergs:turtle4j:1.1.0`

**[Get started with the Turtle4J Java library](docs/getting-started.md)**

For runnable code, see the [Java examples](docs/examples.md), [API overview](docs/api-reference.md), and [Turtle4J 1.1.0 JavaDoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/).

## 4. Need MQTT communication with ROS 2?

**TurtleBridge** is our MQTT ↔ ROS 2 bridge application. It connects MQTT messages to ROS 2 topics through rosbridge, enabling commands and telemetry without requiring MQTT support in ROS 2 itself.

TurtleBridge is **already included in the Docker simulator**. A standalone executable for deployment on a TurtleBot 4's Raspberry Pi is **planned, not yet released**. A physical robot will need a running MQTT-to-ROS 2 bridge for this communication path; direct rosbridge connections do not require TurtleBridge.

**[Learn about TurtleBridge and MQTT](docs/turtlebridge.md)**

## Additional resources

- [Physical TurtleBot 4 setup](docs/turtlebot4-setup.md)
- [ROS 2 runtime discovery](docs/ros2-discovery.md)
- [Developing new functionality with Turtle4J](docs/extending-turtle4j.md)
- [MQTT publish/subscribe examples](docs/mqtt-example.md)
