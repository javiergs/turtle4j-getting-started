# Turtlesim Docker Environment — Setup

This tutorial uses Docker to create a virtual Linux desktop running Ubuntu 22.04 and ROS 2 Humble. The environment includes Turtlesim (a simple 2D robot simulator), rosbridge (for Turtle4J Java WebSocket connections), and TurtleBridge (for MQTT communication). You can access the Linux desktop directly from your web browser using noVNC.

![Docker Environment with TurtleSim, RosBridge and MQTT Bridge](assets/docker-environment.png)

## Step 1 — Install and start Docker

Docker runs the simulation environment on your computer. You only need to install it once.

### A. Install Docker

Choose an installation method for your operating system.

**Option 1 — Graphical interface (GUI)**

On Windows or macOS, download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/). On Linux, follow the [official Docker Engine installation instructions](https://docs.docker.com/engine/install/) for your distribution.

**Option 2 — Command line (CLI)**

If you prefer using a terminal, install Docker using the appropriate command:

**macOS (Homebrew):**

```bash
brew install --cask docker-desktop
```

**Windows (PowerShell):**

```powershell
winget install --id Docker.DockerDesktop -e
```

**Ubuntu Linux:**

First, configure [Docker's official APT repository](https://docs.docker.com/engine/install/ubuntu/). Then run:

```bash
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

The Ubuntu commands require Docker's official repository; they will not work on a fresh Ubuntu installation without that setup.

### B. Start Docker

On Windows or macOS, open **Docker Desktop** and wait until it reports that the engine is running. Installing Docker does not automatically mean it is running.

On macOS, you can also launch Docker Desktop from Terminal:

```bash
open -a Docker
```

On Ubuntu Linux, start Docker Engine with:

```bash
sudo systemctl start docker
```

### C. Verify Docker

Open **Terminal** (macOS/Linux) or **PowerShell** (Windows) and run:

```bash
docker --version
docker info
```

The first command checks that Docker is installed. The second checks that its engine is running. If `docker info` shows **Server** information without a connection error, you are ready.

If it reports **Cannot connect to the Docker daemon**, **failed to connect to the Docker API**, or a missing `docker.sock` file, start Docker Desktop (or Docker Engine on Linux), wait until it is ready, and run `docker info` again. On Linux, you may also need to [configure permission to run Docker without sudo](https://docs.docker.com/engine/install/linux-postinstall/).

**Do not continue to Step 2 until `docker info` succeeds.** You do not need to install Ubuntu or ROS 2 separately.

## Step 2 — Download and start the simulation environment

A Docker **image** is the software package; a **container** is a running instance of that image. Everyone uses the same published image, which includes ROS 2, Turtlesim, rosbridge, and TurtleBridge (MQTT ↔ ROS 2). **You do not need to install ROS 2, Java, or MQTT inside Docker.**

In Terminal (macOS/Linux) or PowerShell (Windows), download the image:

```bash
docker pull ghcr.io/javiergs/turtlesim-docker-environment:latest
```

Start the container using the command for your operating system.

**macOS/Linux:**

```bash
docker run --rm -it \
  --shm-size=1g \
  -e DISPLAY=:1 \
  -p 127.0.0.1:6080:6080 \
  -p 127.0.0.1:9090:9090 \
  ghcr.io/javiergs/turtlesim-docker-environment:latest
```

**Windows PowerShell:**

```powershell
docker run --rm -it `
  --shm-size=1g `
  -e DISPLAY=:1 `
  -p 127.0.0.1:6080:6080 `
  -p 127.0.0.1:9090:9090 `
  ghcr.io/javiergs/turtlesim-docker-environment:latest
```

Keep the terminal open. Docker automatically starts rosbridge, the desktop services, and TurtleBridge. The MQTT bridge can remain running while you learn direct ROS 2 commands.

## Step 3 — Open the Linux desktop

1. On your computer, open a web browser and enter **http://localhost:6080/vnc.html** in its address bar.
2. Click **Connect**.
3. You should see the Linux desktop shown in the image above.

`localhost` means your own computer. The Linux desktop is running inside Docker, but you interact with it through your browser.

## Step 4 — Launch Turtlesim 2D

Double-click **Turtlesim 2D** on the Linux desktop. If prompted, select **Run as a terminal application**. A window containing a turtle should appear.

Turtlesim is a simple ROS 2 program: it draws a turtle and moves it in response to commands. No physical robot is needed.

**Note:** Turtlesim is a 2D environment for learning and experimentation. It does not simulate a full TurtleBot 4 robot.

## Next steps

You now have a running Linux desktop and Turtlesim simulator. **Keep the Docker container and Turtlesim running** while you continue:

1. **[Understanding ROS 2: Nodes, Topics, and Messages](../docs/ros2-basics.md)** — open a terminal inside the Linux desktop, explore ROS 2 commands, and learn what the Keyboard Teleop 2D shortcut does.
2. **[Getting Started with Turtle4J](../docs/getting-started.md)** — connect a Java application through rosbridge at `ws://localhost:9090`.
3. **[MQTT with TurtleBridge](../docs/turtlebridge.md)** — use the same container to publish velocity commands and subscribe to turtle position.

The browser address `http://localhost:6080/vnc.html` opens the Linux desktop. The WebSocket address `ws://localhost:9090` is for Java-to-ROS communication.

## Stop the environment

Return to the terminal that started Docker and press **Ctrl+C**. The `--rm` option in the other commands removes the stopped container, but keeps the downloaded or built image.

## Troubleshooting

- **Docker command not found:** Install Docker, then reopen your terminal.
- **Cannot connect to Docker:** Start Docker Desktop and wait for it to finish launching.
- **Port 6080 or 9090 already in use:** Stop the other application or container using that port.
- **Browser desktop unavailable:** Check that Docker is running and that the startup terminal remains open.
- **ROS 2 command does not move the turtle:** Start Turtlesim first, then run the command in a terminal inside the Linux desktop.
- **Image not found or access denied:** Check the image spelling, network connection, and GitHub package visibility.

