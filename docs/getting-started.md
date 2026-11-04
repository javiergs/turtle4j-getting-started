# Getting started with Turtle4J

This guide shows how to compile and run the Java examples after a ROS 2 environment is ready. If you have not configured an environment yet, choose [Turtlesim in Docker](../docker/README.md) for local practice or [physical TurtleBot 4 setup](turtlebot4-setup.md) for hardware.

If you are new to ROS 2, first complete [Understanding ROS 2](ros2-basics.md).

## Requirements

- Java 21 or newer (the example project targets Java 21).
- Maven.
- A running ROS 2 system with a reachable rosbridge WebSocket server.

Check your Java and Maven installations:

```bash
java -version
mvn -version
```

## Obtain the examples

You can download the project without Git:

1. Open the [repository home page](https://github.com/javiergs/turtle4j-getting-started).
2. Select **Code → Download ZIP**.
3. Extract the ZIP file.
4. Open a terminal in the extracted repository folder (the folder containing `pom.xml`).

Alternatively, if you already use Git:

```bash
git clone https://github.com/javiergs/turtle4j-getting-started.git
cd turtle4j-getting-started
```

## Compile the project

From the folder containing `pom.xml`:

```bash
mvn compile
```

Maven automatically downloads the published TURTLE4J library and the other declared dependencies.

## Connect to ROS 2

Keep your ROS 2 environment and rosbridge running. The local Docker setup exposes rosbridge at `ws://localhost:9090`. For a physical robot, use the reachable IP address of the computer hosting rosbridge, such as `ws://ROBOT_IP:9090`.

**Note:** `localhost` always refers to the computer running the Java program. It does not refer to a remote robot.

## Run your first example

From the folder containing `pom.xml`:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

The example connects to `ws://localhost:9090` by default and prints a discovery status. To use a remote rosbridge host:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot -Dexec.args=ws://ROBOT_IP:9090
```

For more programs, see the [Java examples guide](examples.md). The included motion examples are designed for Turtlesim 2D; do not run them unchanged on physical hardware.

## Use Turtle4J in your own Maven project

The published Maven Central coordinates are `com.javiergs:turtle4j:1.1.0`. Add this dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Troubleshooting

- **Java or Maven not found:** Install the tools, reopen your terminal, and check `java -version` and `mvn -version`.
- **No `pom.xml`:** Change into the extracted repository root before running Maven.
- **Connection refused:** Check that ROS 2 and rosbridge are running and port `9090` is reachable.
- **Discovery is unavailable:** Some discovery features require `rosapi`. See [ROS 2 runtime discovery](ros2-discovery.md).
- **No pose messages:** Confirm Turtlesim is running and publishing `/turtle1/pose`.
