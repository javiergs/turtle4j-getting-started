# Turtlesim 2D Docker environment

This local test environment bundles **Ubuntu 22.04, ROS 2 Humble, rosbridge, Turtlesim 2D, LXDE/noVNC**. It is a 2D development environment, **not** a Gazebo or TurtleBot 4 physical robot simulator.


<img width="4608" height="3072" alt="docker only" src="https://github.com/user-attachments/assets/75c8e3e9-be42-43b4-a505-51c06997ead0" />


## Start

Start Docker Desktop (macOS/Windows) or the Docker engine (Linux), then from the repository root:

```bash
cd docker
docker compose up --build
```

The first build may take some time. After the build, open the [TURTLE4J desktop](http://localhost:6080/vnc.html) in your browser and click **Connect**.

Double-click the **Turtlesim 2D** desktop shortcut to launch the simulator. If LXDE asks how to open the shortcut, choose **Run as a terminal application**. Wait for the Turtlesim window to appear. To control the turtle manually, double-click **Keyboard Teleop 2D**, choose **Run as a terminal application** if prompted, and keep the teleop terminal focused while using the keyboard.

The rosbridge WebSocket endpoint is **ws://localhost:9090**.

The Compose configuration binds these ports to localhost:

- **6080:** noVNC web desktop.
- **5900:** VNC (unauthenticated; local only).
- **9090:** rosbridge WebSocket (unauthenticated; local only).

## Included Desktop Shortcuts

- **Turtlesim 2D:** Launches the ROS 2 turtle simulator.
- **Keyboard Teleop 2D:** Publishes velocity commands to `/turtle1/cmd_vel`; launch Turtlesim first and focus the teleop terminal.



## Desktop wallpaper

The Docker image uses the included `docker/turtle4j-wallpaper.png` as its LXDE desktop background.
