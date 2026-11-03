# Docker Setup for Turtlesim 2D Environment

This tutorial shows you how to:

* Set up a virtual Linux desktop on your computer using Docker.
* Launch the Turtlesim 2D simulator.
* Control the turtle using ROS 2 command line instructions
* Connect Java applications to ROS 2 using **Turtle4J**.

# What are we installing?

* Docker: Runs the container that hosts the simulation environment.
* Ubuntu 22.04: Provides the Linux operating system inside the container.
* ROS 2 Humble: Enables communication between robotics applications.
* Turtlesim: Simulates a turtle moving in a two-dimensional environment.
* rosbridge: Connects external applications, such as Turtle4J, to ROS 2.
* noVNC: Provides access to the Linux desktop through a web browser.

<img width="80%" alt="turtle docker" src="https://github.com/user-attachments/assets/5d739a90-be13-4c90-a975-5be11e8bd5e7" />

## Prerequisites

Before setting up the simulation environment, install **Docker** on your computer:

1. On Windows or macOS, install [Docker Desktop](https://www.docker.com/products/docker-desktop/). On Linux, install Docker Engine with the Docker Compose plugin.
2. Start Docker Desktop (or make sure Docker Engine is running on Linux).
3. Open **Terminal** on macOS/Linux or **PowerShell** on Windows.
4. Check that Docker is available:

```bash
docker --version
docker compose version
```

Both commands should display version information. If they do not, check your Docker installation before continuing. You do **not** need to install Ubuntu or ROS 2 separately.

## Set up the Docker environment

There are two ways to prepare the same simulation environment. **Choose only one.**

- **Method 1 — Use a ready-made image:** Docker downloads an already prepared environment. This will be the easiest method once the image is published.
- **Method 2 — Build from downloaded files:** Download a few files from this repository and let Docker prepare the environment on your computer.

**For now, use Method 2.** The ready-made image for Method 1 has not yet been verified as publicly available.

### Method 1 — Use a ready-made image (not yet available)

A *Docker image* is a prepared package of software. Once the image is published on GitHub Container Registry (GHCR), you will be able to start it with one command, without downloading repository files.

**Do not use this command yet:** The image name below is provisional, and its public availability has not been confirmed.

```bash
docker run --rm -it -p 127.0.0.1:6080:6080 -p 127.0.0.1:9090:9090 ghcr.io/javiergs/turtle4j-ros2:latest
```

When the image is published, Docker will download it automatically the first time you run the command. Keep the terminal open while the environment is running. Then continue to **Open the desktop**.

### Method 2 — Build from downloaded files (recommended now)

You do not need Git or any experience with GitHub commands.

**Step 1. Download the files**

1. Open the [Turtle4J Getting Started repository](https://github.com/javiergs/turtle4j-getting-started).
2. Click the green **Code** button and select **Download ZIP**.
3. Find the downloaded ZIP file and extract it.
4. Open the extracted folder, then open the `docker` folder.

Inside `docker`, you should see these files:

- `Dockerfile` — Tells Docker how to prepare the environment.
- `compose.yaml` — Tells Docker how to start it.
- `supervisord.conf` — Starts the programs needed inside the container.
- `turtle4j-wallpaper.png` — Provides the desktop background.

Leave these files together in the `docker` folder.

**Step 2. Open a terminal in the docker folder**

Open Terminal (macOS/Linux) or PowerShell (Windows), and navigate to the extracted `docker` folder. For example, if you extracted the ZIP into Downloads:

```bash
cd ~/Downloads/turtle4j-getting-started-main/docker
```

The folder location may be different on your computer. On Windows PowerShell, you can type `cd ` (with a space), drag the `docker` folder into the window to insert its full path, and press Enter.

**Step 3. Build and start the environment**

With Docker running and your terminal inside the `docker` folder, enter:

```bash
docker compose up --build
```

Docker downloads the necessary software, builds the environment, and starts the container. The first build can take several minutes or longer, depending on your internet connection and computer.

Keep this terminal open. When the startup messages settle, continue to **Open the desktop**.

## Open the desktop

1. Open **http://localhost:6080/vnc.html** in a browser **on your computer**.
2. Click **Connect**.
3. You should see the Linux desktop pictured above.

Here, `localhost` means your own computer. The browser is showing a desktop running inside Docker.

## Activity 1 — Start the turtle

Double-click **Turtlesim 2D** on the Linux desktop. If prompted, select **Run as a terminal application**. A window containing a turtle should appear.

Turtlesim is a simple ROS 2 program: it draws a turtle and moves it in response to commands. No physical robot is needed.

## Activity 2 — Move the turtle

1. Keep Turtlesim running.
2. Double-click **Keyboard Teleop 2D** and select **Run as a terminal application** if asked.
3. Click inside the keyboard-control terminal so it receives your keystrokes.
4. Use the keys shown in that terminal to move and turn the turtle.

**What happened?** ROS 2 programs exchange messages using named channels called **topics**. The keyboard program **publishes** movement commands to the topic `/turtle1/cmd_vel`. Turtlesim receives them and moves the turtle. A velocity command tells the turtle how fast to move or rotate, not where to go.

```text
Keyboard → Keyboard controller → ROS 2 topic /turtle1/cmd_vel → Turtlesim → Turtle moves
```

## Activity 3 — Connect a Java application with Turtle4J

You can later replace the keyboard controller with your own Java application:

```text
Java application → Turtle4J → rosbridge → ROS 2 → Turtlesim
```

The **rosbridge WebSocket** endpoint for a Java application running on your computer is:

```text
ws://localhost:9090
```

**Two different addresses:** `http://localhost:6080/vnc.html` opens the Linux desktop in your browser; `ws://localhost:9090` is for Java-to-ROS communication.

## Stop the environment

Return to the terminal that started Docker and press **Ctrl+C**. With Method 2, you can also run `docker compose down` from the same folder. With Method 1, `--rm` removes the stopped container, but the downloaded image remains available.

## Troubleshooting

- **Docker command not found:** Install Docker, then reopen your terminal.
- **Cannot connect to Docker:** Start Docker Desktop and wait for it to finish launching.
- **Port 6080 or 9090 already in use:** Stop the other application or container using that port.
- **Browser desktop unavailable:** Check that Docker is running and that the startup terminal remains open.
- **Keyboard does not move the turtle:** Start Turtlesim first and click the keyboard-control terminal to give it focus.
- **Method 1 says image not found or access denied:** The GHCR package may not be published or public yet. Use Method 2.

## For maintainers: publishing the ready-made image

The existing Dockerfile supports local builds, but **a Dockerfile is not itself a published image**. Publish the image to GitHub Container Registry using a maintainer workflow with permission to push packages (for example, GitHub Actions with `packages: write`). Make the package **public**, test `docker pull` and `docker run` on a clean computer, and verify support for both `linux/amd64` and `linux/arm64` before promising cross-platform compatibility. Once published, replace the provisional warning above with the verified image name and a stable version tag.
