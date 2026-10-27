# ROS 2 runtime discovery

TURTLE4J does **not** assume that a particular robot has battery, LiDAR, odometry, or other interfaces. It asks the running ROS 2 graph through **rosapi**, accessed via rosbridge.

## Discovery lifecycle

`TurtleClient.connect()` opens the WebSocket connection and attempts discovery. It returns a `RobotCapabilities` snapshot. Discovery calls `/rosapi/topics` (topic names and message types) and `/rosapi/services` (service names). The library does not automatically poll for changes.

```java
TurtleClient client = new TurtleClient("ws://localhost:9090");
try {
    RobotCapabilities report = client.connect();
    System.out.println(report.getDiscoveryStatus());
    System.out.println(report.getAvailable());
    System.out.println(report.getUnavailable());
    System.out.println(report.getAmbiguous());
    System.out.println(report.getExtras());
    System.out.println(report.getServices());
    RobotCapabilities updated = client.refreshCapabilities();
} finally {
    client.close();
}
```

Import `com.javiergs.turtle4j.TurtleClient` and `RobotCapabilities`.

## What the report means

- **AVAILABLE:** exactly one discovered topic matches a recognized capability.
- **NOT_DETECTED:** discovery completed, but no topic matched.
- **AMBIGUOUS:** multiple topics matched the same capability; none is chosen automatically.
- **UNKNOWN:** discovery failed or returned an unexpected response, so absence cannot be established.

`getAvailable()` maps recognized capabilities to their discovered `RosInterface` (name and message type). `getUnavailable()` lists capabilities not detected in a complete scan. `getAmbiguous()` lists capabilities with multiple matches. `getExtras()` preserves unmatched and ambiguous topics. `getServices()` contains discovered **service names only**, not service types.

The current dictionary recognizes **BATTERY**, **LIDAR**, **IMU**, **ODOMETRY**, **CAMERA**, and **VELOCITY** based on topic message types and, for velocity, a `cmd_vel` name hint. These are topic-level indicators, **not proof** that a robot function is operational or that a velocity topic has an active subscriber.

## Using a discovered capability

```java
client.subscribe(TurtleCapability.BATTERY,
        battery -> System.out.println(battery.toPrettyString()));
```

This resolves the topic and message type from the latest capability report. If discovery is unknown, the capability is not detected, or multiple matching topics exist, the overload throws rather than guessing. In that case, use the explicit form:

```java
client.subscribe("/robot1/battery_state", "sensor_msgs/msg/BatteryState",
        battery -> System.out.println(battery.toPrettyString()));
```

If rosapi is unavailable, raw `publish(topic, type, message)` and `subscribe(topic, type, callback)` still work after the WebSocket connects. A missing rosapi service can delay discovery until its request times out.

## Current limitations

The report does not discover ROS actions, service types, or all possible ROS 2 interfaces. It does not infer docking or navigation availability from service names, and it does not provide motion control or collision avoidance. See the [API reference](api-reference.md).
