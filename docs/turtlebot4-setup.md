# Physical TurtleBot 4 — ROS 2 connection setup

This guide is for connecting a Java application using Turtle4J to an existing physical TurtleBot 4 through a reachable ROS 2 computer. If you are learning for the first time, start with the [Docker Turtlesim environment](../docker/README.md).

## Before you begin

You need a configured TurtleBot 4 and access to a ROS 2 computer on its network. The computer hosting **rosbridge** may be the robot's computer or another ROS 2 computer able to communicate with the robot. Do not assume rosbridge is installed or running by default.

Confirm the ROS 2 distribution, network connectivity, and relevant robot topics using your TurtleBot 4 setup documentation. The commands below assume **ROS 2 Humble on Ubuntu**.

## Install and start rosbridge

On the ROS 2 computer that will host rosbridge:

```bash
sudo apt update
sudo apt install ros-humble-rosbridge-suite
source /opt/ros/humble/setup.bash
ros2 launch rosbridge_server rosbridge_websocket_launch.xml
```

Keep the server running. The default WebSocket port is `9090`. Ensure your Java computer can reach the host over the network. Do not expose rosbridge to untrusted networks.

## Connect from Java

Use the reachable IP address of the rosbridge host:

```java
import com.javiergs.turtle4j.TurtleClient;

TurtleClient client = new TurtleClient("ws://<ROSBRIDGE_HOST_IP>:9090");
```

You can also use the supplied Java connection example from the repository root:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot -Dexec.args=ws://ROBOT_IP:9090
```

Here, `ROBOT_IP` is a placeholder for the **rosbridge host's** IP address; that host is not necessarily the robot itself. See the [Java Getting Started guide](getting-started.md) for installation and compilation.

## Verify capabilities before motion

Check network reachability, ROS 2 namespaces, available topics, and message types. Runtime capability discovery additionally requires **rosapi**; raw publish/subscribe through rosbridge does not. See [ROS 2 discovery](ros2-discovery.md).

**Safety:** The included Turtlesim motion examples are not ready-to-run TurtleBot 4 navigation programs. Do not execute them unchanged on physical hardware. Verify the actual motion topic, use conservative speeds, keep the robot in a clear supervised area, and maintain a reliable physical stop mechanism.
