# Getting started

TURTLE4J is a Java client for an existing ROS 2 rosbridge WebSocket server. Choose one of two environments below. Both use the same Java API.

## Option A: Turtlesim in Docker

Use the included ROS 2 Humble + rosbridge + Turtlesim 2D desktop for development without a robot. This is **not** a full TurtleBot 4 simulator.

1. Follow the [Docker setup guide](../docker/README.md).
2. Start the container and launch **Turtlesim 2D** from its browser desktop.
3. The Java application connects to `ws://localhost:9090`.
4. [Run a Java example](examples.md).

The Docker environment is optional and does not require Maven to start.

## Option B: Physical TurtleBot 4

A ROS 2 computer on the robot's network must run rosbridge. This may be the robot's computer or another reachable ROS 2 computer. Do **not** assume rosbridge is preinstalled on the robot.

For ROS 2 Humble, on the computer hosting rosbridge:

```bash
sudo apt update
sudo apt install ros-humble-rosbridge-suite
source /opt/ros/humble/setup.bash
ros2 launch rosbridge_server rosbridge_websocket_launch.xml
```

Connect to the reachable rosbridge host (default WebSocket port: 9090):

```java
TurtleClient client = new TurtleClient("ws://<ROSBRIDGE_HOST_IP>:9090");
```

Import `com.javiergs.turtle4j.TurtleClient`. For a remote robot, `localhost` refers to the computer running Java, not the robot. Verify network reachability, ROS 2 namespaces, and the actual motion topic before commanding movement. Use conservative speeds and maintain a reliable way to stop the robot. The bundled Turtlesim motion examples are **not** ready-to-run physical TurtleBot 4 navigation programs.

Runtime capability discovery additionally requires **rosapi** to be running; rosbridge alone is sufficient for raw publish/subscribe. See [ROS 2 discovery](ros2-discovery.md).

## Java requirements and Maven installation

- **Java 21 or newer** (the Maven project targets Java 21).
- **Maven**.
- A reachable rosbridge WebSocket endpoint.

From the repository root:

```bash
mvn clean install
```

The published Maven Central coordinates are `com.javiergs:turtle4j:1.0.0`. To use the library in another Maven project:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Developer tests

Library unit and integration tests remain in the private `api-turtle4j` repository. To run examples, first start the Docker environment and launch Turtlesim 2D.

```bash
mvn compile && mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

The integration test uses `ws://localhost:9090` and requires a running Turtlesim node. It sends a short movement command; do not point it at physical hardware without reviewing the test.
