# Understanding ROS 2: Nodes, Topics, and Messages

**Before you begin:** Complete [Docker Setup for Turtlesim](../docker/README.md). Keep the Docker container running, open the Linux desktop at `http://localhost:6080/vnc.html`, and launch **Turtlesim 2D**. All commands on this page run in a terminal **inside the browser-based Linux desktop**, not in your Mac/Windows terminal.

In this tutorial, you will:

1. Discover running ROS 2 nodes and topics.
2. Observe messages as the turtle moves.
3. Publish movement commands from the terminal.
4. Understand how **Keyboard Teleop 2D** works.

## Step 1 — Discover nodes

Open a terminal inside the Linux desktop and enter:

```bash
ros2 node list
```

**Notice:** You should see `/turtlesim` (other nodes may also appear). A **node** is a running ROS 2 program that participates in communication.

## Step 2 — Discover topics

In the same terminal, enter:

```bash
ros2 topic list
```

**Notice:** Look for `/turtle1/cmd_vel` and `/turtle1/pose`. A **topic** is a named channel that nodes use to exchange messages.

Inspect the command topic:

```bash
ros2 topic info /turtle1/cmd_vel
```

**Notice:** Its message type is `geometry_msgs/msg/Twist`. The output also shows publisher and subscriber counts; these can change depending on which applications are running.

## Step 3 — Watch messages in real time

Open a **second terminal** inside the Linux desktop. Enter:

```bash
ros2 topic echo /turtle1/pose
```

**Notice:** You should see changing fields such as `x`, `y`, and `theta` (orientation), even while the turtle is stationary. Keep this terminal open.

## Step 4 — Publish movement commands

Return to the **first terminal** and enter:

```bash
ros2 topic pub --once /turtle1/cmd_vel geometry_msgs/msg/Twist '{linear: {x: 2.0}, angular: {z: 0.0}}'
```

**Notice:** The turtle should move forward briefly, and the pose values in the second terminal should change. The command sends **one** velocity message, so movement may be small.

Now try turning:

```bash
ros2 topic pub --once /turtle1/cmd_vel geometry_msgs/msg/Twist '{linear: {x: 0.0}, angular: {z: 2.0}}'
```

**Notice:** The turtle should rotate briefly. `linear.x` specifies forward speed; `angular.z` specifies turning speed. These are velocities, **not** target positions.

To observe more obvious continuous movement, you can publish repeatedly:

```bash
ros2 topic pub --rate 2 /turtle1/cmd_vel geometry_msgs/msg/Twist '{linear: {x: 1.0}, angular: {z: 0.0}}'
```

Watch the turtle and pose values, then press **Ctrl+C** in the first terminal to stop publishing. The turtle may continue briefly until the simulator stops applying the last velocity command.

## Step 5 — Understand Keyboard Teleop 2D

**Keyboard Teleop 2D is not Turtle4J.** It is a desktop shortcut that launches the existing ROS 2 program `teleop_twist_keyboard`. The shortcut itself is a text configuration file (`.desktop`); it is not a separate simulator.

You can run the same program yourself. Stop any continuous publisher from Step 4, then enter in the first terminal:

```bash
ros2 run teleop_twist_keyboard teleop_twist_keyboard --ros-args -r /cmd_vel:=/turtle1/cmd_vel
```

**Notice:** The terminal displays keyboard instructions. Click **inside that terminal** and use the displayed movement keys (for example, `i` for forward). Watch the turtle move and the `/turtle1/pose` values update in your second terminal. Press **Ctrl+C** to exit teleoperation.

The `-r /cmd_vel:=/turtle1/cmd_vel` option redirects the program's velocity messages to the topic that Turtlesim listens to. If the desktop shortcut opens as text rather than launching, using the command above works without the shortcut.

## What did you learn?

- A **node** is a running ROS 2 program, such as `/turtlesim`.
- A **topic** is a named channel, such as `/turtle1/cmd_vel`.
- A **message** is structured data published on a topic, such as a `geometry_msgs/msg/Twist` velocity command.
- **Publishers** send messages; **subscribers** receive them.

```text
Keyboard → teleop_twist_keyboard → /turtle1/cmd_vel → Turtlesim
Terminal → ros2 topic pub      → /turtle1/cmd_vel → Turtlesim
```

Both approaches control the same turtle through the same ROS 2 topic. Next, you will send commands from Java instead.

## Next step — Getting Started with Turtle4J

Continue with the [Java Getting Started guide](getting-started.md). Keep Docker and Turtlesim running. Your Java application connects through rosbridge at `ws://localhost:9090`:

```text
Java application → Turtle4J → rosbridge → ROS 2 → Turtlesim
```
