# Student Quick Start — TURTLE4J 1.1.0

**Goal:** Learn ROS 2 communication with Java in a safe 2D simulator, then develop new functions **outside the TURTLE4J library** for possible future integration.

> [!IMPORTANT]
> The Docker environment runs **Turtlesim 2D**, not a full TurtleBot 4 simulator. Complete the exercises here before considering physical hardware. Do not run simulator motion examples unchanged on a real robot.

## 1. Install and check prerequisites

Install Docker Desktop, JDK 21 or newer, Maven, and Git. In a terminal, verify:

```bash
docker --version
docker compose version
java -version
mvn -version
git --version
```

Start Docker Desktop and wait until its engine is running.

## 2. Clone the runnable examples

```bash
git clone https://github.com/javiergs/turtle4j-getting-started.git
cd turtle4j-getting-started
mvn clean compile
```

Maven downloads the published `com.javiergs:turtle4j:1.1.0` dependency. **Do not clone or edit the library source** for these exercises.

## 3. Start the ROS 2 simulator

In terminal A, from the examples repository root:

```bash
cd docker
docker compose up --build
```

Keep this terminal open. In a browser, open **http://localhost:6080/vnc.html**, click **Connect**, and double-click **Turtlesim 2D**. If prompted, select **Run as a terminal application**. Wait until the simulator window appears.

The simulator desktop is served on port 6080; rosbridge is available at `ws://localhost:9090`. Ports are bound to the local computer.

## 4. Connect from Java

In a **second terminal**, navigate to the **examples repository root** (not the `docker/` folder):

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

**Expected:** The program connects and reports a discovery status. If it cannot connect, verify Docker is running and rosbridge is listening on port 9090. If discovery is UNKNOWN, check that the simulator and ROS 2 discovery services are running; see [ROS 2 discovery](ros2-discovery.md).

Then run:

```bash
mvn exec:java -Dexec.mainClass=examples.DiscoverRobot
```

**Expected:** A list of discovered topics, services, and/or capabilities. Note the topic names and message types you observe.

## 5. Receive data before sending commands

```bash
mvn exec:java -Dexec.mainClass=examples.ReadPose
```

**Expected:** Pose updates from `/turtle1/pose` for approximately ten seconds. Find the `subscribe(...)` call in [ReadPose.java](../src/main/java/examples/ReadPose.java). Record the topic, message type, and fields you receive.

## 6. Send commands in the simulator

Run each command separately and observe the simulator after each:

```bash
mvn exec:java -Dexec.mainClass=examples.MoveTurtle
mvn exec:java -Dexec.mainClass=examples.RotateTurtle
mvn exec:java -Dexec.mainClass=examples.CirclePath
```

Next try `examples.KeyboardControl` (the control window opens on your Java computer), followed by `examples.MoveDistance` and `examples.RotateAngle`. See the [examples guide](examples.md) for the complete sequence and safety notes.

**Checkpoint:** Explain the difference between **publishing** a command and **subscribing** to pose feedback. Inspect [MoveTurtle.java](../src/main/java/examples/MoveTurtle.java) and the raw-publishing helper [VelocityCommands.java](../src/main/java/examples/VelocityCommands.java). Identify the ROS topic and message type used for velocity commands.

## 7. Develop an independent Java experiment

Create a **separate Java/Maven project** in your own repository. Depend on the published library:

```xml
<dependency>
    <groupId>com.javiergs</groupId>
    <artifactId>turtle4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

Start with one small, testable behavior. Examples: read and print pose; send a short, bounded velocity command; implement a `turnLeft()` helper; or attempt `goToPosition(x, y)` using feedback.

Use `TurtleClient.connect()`, `subscribe(topic, type, callback)`, and `publish(topic, type, message)` to explore raw ROS communication. You may also compare your approach to the existing `setVelocity()`, `move()`, and `rotate()` API methods. **Do not add code to the TURTLE4J library yet.**

For every new function, document:

- Function signature, parameters, and intended behavior.
- ROS topic(s), message type(s), and sample payload.
- Whether the function publishes, subscribes, or both.
- Feedback used to determine success; timeouts and failure behavior.
- Simulator-specific assumptions versus possible robot-independent behavior.
- A reproducible run command and brief evidence (terminal output or screenshot).

Keep motion bounded and ensure the simulator can be stopped. Treat `stop()` as a command, not a safety guarantee.

## 8. Optional: explore MQTT

After completing the ROS exercises, follow [MQTT publish/subscribe](mqtt-example.md). TURTLE4J 1.1.0 supports an **independent MQTT connection**, but does **not** automatically translate or forward messages between MQTT and ROS 2. Do not use the public broker to control a physical robot.

## 9. What to submit for review

Provide a link to your independent repository, its README with exact setup/run instructions, and a short report describing:

1. Which supplied examples worked and any setup difficulties.
2. At least one independently implemented function and how you tested it.
3. ROS topic/message evidence, observed output, and limitations.
4. Which functionality you think could later be encapsulated in TURTLE4J, and why.

The goal is **experimentation and reusable design proposals**, not changes to the published 1.1.0 API.
