# Understanding ROS 2: Nodes, Topics, and Messages

## Discover ROS 2 Nodes

Run the following commands in a terminal inside the browser-based Linux desktop, not in your computer's Terminal or PowerShell. Enter:

```bash
ros2 node list
```

**Notice:** You should see `/turtlesim` (other nodes may also appear). A **node** is a running ROS 2 program that participates in communication.

## Explore ROS 2 Topics

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

## Observe Messages

Open a **second terminal** inside the Linux desktop. Enter:

```bash
ros2 topic echo /turtle1/pose
```

**Notice:** You should see changing fields such as `x`, `y`, and `theta` (orientation), even while the turtle is stationary. Keep this terminal open.

## Publish Movement Commands

Return to the **first terminal** and enter:

```bash
ros2 topic pub --once \
  /turtle1/cmd_vel \
  geometry_msgs/msg/Twist \
  '{linear: {x: 2.0}, angular: {z: 0.0}}'
```

**Notice:** The turtle should move forward briefly, and the pose values in the second terminal should change. The command sends **one** velocity message, so movement may be small.

Now try turning:

```bash
ros2 topic pub --once \
  /turtle1/cmd_vel \
  geometry_msgs/msg/Twist \
  '{linear: {x: 0.0}, angular: {z: 2.0}}'
```

**Notice:** The turtle should rotate briefly. `linear.x` specifies forward speed; `angular.z` specifies turning speed. These are velocities, **not** target positions.

To observe more obvious continuous movement, you can publish repeatedly:

```bash
ros2 topic pub --rate 2 \
  /turtle1/cmd_vel \
  geometry_msgs/msg/Twist \
  '{linear: {x: 1.0}, angular: {z: 0.0}}'
```

Watch the turtle and pose values, then press **Ctrl+C** in the first terminal to stop publishing. The turtle may continue briefly until the simulator stops applying the last velocity command.

## Control the Turtle with the Keyboard

**Keyboard Teleop 2D is not Turtle4J.** It is a desktop shortcut that launches the existing ROS 2 program `teleop_twist_keyboard`. The shortcut itself is a text configuration file (`.desktop`); it is not a separate simulator.

You can run the same program yourself. Stop any continuous publisher from the previous section, then enter in the first terminal:

```bash
ros2 run teleop_twist_keyboard teleop_twist_keyboard --ros-args -r /cmd_vel:=/turtle1/cmd_vel
```

**Notice:** The terminal displays keyboard instructions. Click **inside that terminal** and use the displayed movement keys (for example, `i` for forward). Watch the turtle move and the `/turtle1/pose` values update in your second terminal. Press **Ctrl+C** to exit teleoperation.

The `-r /cmd_vel:=/turtle1/cmd_vel` option redirects the program's velocity messages to the topic that Turtlesim listens to. If the desktop shortcut opens as text rather than launching, using the command above works without the shortcut.

## What Did You Learn?

- A **node** is a running ROS 2 program, such as `/turtlesim`.
- A **topic** is a named channel used to exchange information, such as `/turtle1/cmd_vel`.
- A **message** contains structured data, such as a `geometry_msgs/msg/Twist` movement command.
- **Publishers** send messages to topics, while **subscribers** receive them.

In this tutorial, you controlled the turtle in two ways: by publishing movement commands directly from the terminal and by using the keyboard. Both methods publish messages to `/turtle1/cmd_vel`, which Turtlesim subscribes to. When Turtlesim receives a movement command, it moves the turtle.
