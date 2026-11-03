# Developing New Functionality with Turtle4J

This guide is for developers who have completed the [Java Getting Started guide](getting-started.md) and tried the [runnable examples](examples.md). The goal is to experiment with new functionality in **your own Java project**, evaluate it in Turtlesim, and identify ideas that could eventually be useful in Turtle4J.

Turtlesim is a 2D learning simulator, **not** a full TurtleBot 4 simulation. Do not run simulator motion examples unchanged on physical hardware.

## 1. Create an independent Java project

Create a separate Maven project in your own repository. Depend on the published Turtle4J library rather than editing its source:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

Keep your Docker simulator running and connect to rosbridge at `ws://localhost:9090`. You can use the examples repository as a reference, but your experiments should remain independent.

## 2. Start with one testable behavior

Choose a small experiment, for example:

- Read and display the turtle's current pose.
- Publish a short, bounded velocity command.
- Implement a reusable `turnLeft()` helper.
- Attempt `goToPosition(x, y)` using pose feedback.

Explore `TurtleClient.connect()`, `subscribe(topic, type, callback)`, and `publish(topic, type, message)` for raw ROS 2 communication. Compare your implementation with the existing `setVelocity()`, `move()`, and `rotate()` API methods.

For inspiration, inspect [MoveTurtle.java](../src/main/java/examples/MoveTurtle.java), [ReadPose.java](../src/main/java/examples/ReadPose.java), and [VelocityCommands.java](../src/main/java/examples/VelocityCommands.java).

## 3. Document and test your experiment

For each new function, document:

- The method signature, parameters, and expected behavior.
- ROS 2 topics, message types, and example payloads.
- Whether it publishes, subscribes, or both.
- How feedback determines success, including timeouts and failure behavior.
- Which assumptions depend on Turtlesim rather than a physical robot.
- Exact run instructions and brief evidence of the observed behavior.

Test normal behavior and at least one failure case. Keep movements bounded, and ensure the simulator can be stopped. A `stop()` call sends a command; it is **not** a safety guarantee if communication is interrupted.

## 4. Evaluate whether the function is reusable

Consider whether the behavior belongs in a general-purpose library or is specific to your application. A useful proposal explains what problem it solves, what assumptions it makes, and how it could be tested across different ROS 2 environments.

**Do not modify the published Turtle4J library as part of this exercise.** Build and evaluate the behavior independently first.

## Optional — Explore MQTT

After the ROS 2 exercises, try the [MQTT publish/subscribe example](mqtt-example.md). Turtle4J supports an independent MQTT connection; it does **not** automatically forward or translate messages between MQTT and ROS 2. Do not use a public MQTT broker to control a physical robot.
