# API reference

This is a concise guide to the current Java API. The [published TURTLE4J 1.1.0 Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.1.0/) is the authoritative public API reference.

## TurtleClient

`com.javiergs.turtle4j.TurtleClient` is the recommended application entry point.

| Method | Purpose |
| --- | --- |
| `new TurtleClient(websocketUrl)` | Configure the rosbridge WebSocket endpoint. |
| `connect()` | Connect and return a `RobotCapabilities` snapshot; raw messaging still works if discovery fails. |
| `publish(topic, type, message)` | Send a JSON-serializable ROS 2 message to an explicit topic. |
| `setVelocity(linear, angular)` | Publish a Twist velocity command to the uniquely discovered velocity topic (m/s and rad/s). |
| `stop()` | Publish zero linear and angular velocities to the discovered topic. |
| `move(meters)` | Blocking signed-distance motion using Turtlesim pose or ROS odometry. |
| `rotate(degrees)` | Blocking signed-angle motion using Turtlesim pose or ROS odometry. |
| `subscribe(topic, type, callback)` | Subscribe to an explicit topic and message type. |
| `subscribe(capability, callback)` | Subscribe using a uniquely discovered capability. |
| `unsubscribe(topic)` | Remove a topic subscription and its callback. |
| `discoverTopics()` | Query rosapi for topic names and message types. |
| `discoverServices()` | Query rosapi for service names. |
| `refreshCapabilities()` | Re-run discovery and return a new snapshot. |
| `getCapabilities()` | Return the most recent snapshot without querying ROS 2. |
| `close()` | Close the WebSocket connection. |

The raw subscription callback receives a Jackson `JsonNode`. Only one callback per topic is supported per connection: subscribing twice to the same topic now raises an error rather than silently replacing the callback. Call `unsubscribe(topic)` before subscribing again. Unsubscribing an unknown topic is a no-op. Feedback-based `move()` and `rotate()` release their pose subscriptions after each operation. Use a `try/finally` block to close the client.

## RobotCapabilities

`RobotCapabilities` is a snapshot, not a live view. Key methods include `getDiscoveryStatus()`, `getError()`, `getAvailability(capability)`, `isAvailable(capability)`, `getInterface(capability)`, `getAvailable()`, `getUnavailable()`, `getAmbiguous()`, `getExtras()`, and `getServices()`.

Discovery status is `COMPLETE` or `UNKNOWN`. Per-capability availability is `AVAILABLE`, `NOT_DETECTED`, `AMBIGUOUS`, or `UNKNOWN`.

## TurtleCapability and RosInterface

`TurtleCapability` is an enum of recognized topic capabilities: `BATTERY`, `LIDAR`, `IMU`, `ODOMETRY`, `CAMERA`, and `VELOCITY`. The enum does not assert that a robot supports them.

`RosInterface` represents a discovered topic with `getName()` and `getType()`.

## RosbridgeConnection

`RosbridgeConnection` is the lower-level transport used by `TurtleClient`. It supports raw publish/subscribe, service calls, and rosapi discovery. Application code should generally use `TurtleClient`.

**Feedback motion:** `move(meters)` and `rotate(degrees)` require a unique discovered velocity topic plus a unique Turtlesim pose or odometry topic. They are blocking, have a 15-second timeout, and attempt a zero-velocity command on exit. They are educational controllers, not collision avoidance or safety-rated navigation. `setVelocity()` and `stop()` require an unambiguous discovered `geometry_msgs/msg/Twist` topic ending in `/cmd_vel`. They do not automatically refresh discovery or verify that a physical robot actually stopped. Existing motion examples publish raw ROS 2 velocity messages.
