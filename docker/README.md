# Docker Setup for Turtlesim 2D Environment

This tutorial shows you how to:

* Set up a virtual Linux desktop on your computer using Docker.
* Launch the Turtlesim 2D simulator.
* Control the turtle using ROS 2 command line instructions
* Connect Java applications to ROS 2 using **Turtle4J**.

# What are we installing?

- **Docker** runs a packaged software environment (a *container*) on your computer.
- **Ubuntu 22.04** is the Linux operating system inside that container.
- **ROS 2 Humble** lets robotics programs communicate.
- **Turtlesim** shows a turtle moving in a two-dimensional window.
- **rosbridge** allows external programs, including Java applications using Turtle4J, to communicate with ROS 2.
- **Browser desktop (noVNC)** lets you interact with the Linux desktop from your regular web browser.

<img width="70%" alt="Linux desktop provided by the Docker environment" src="https://github.com/user-attachments/assets/75c8e3e9-be42-43b4-a505-51c06997ead0" />

## Prerequisite: install and start Docker

Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) on Windows or macOS, or Docker Engine on Linux. **Start Docker** and wait until it is running. Open Terminal (macOS/Linux) or PowerShell (Windows).

Choose **Method 1** if a prebuilt image is available; otherwise use **Method 2**. These are two ways to start the same Docker environment, not the two ROS 2 environment choices on the main README.

## Method 1 — Download a ready-made image (recommended once published)

A *Docker image* is a ready-made package containing the software needed for the environment. We plan to publish one on **GitHub Container Registry (GHCR)**, so you will not need to download source files, clone the repository, or build anything.

**Not yet verified as published:** The following image name is **provisional**. Until the GHCR package is published and publicly accessible, use **Method 2**.

To download the image:

```bash
docker pull ghcr.io/javiergs/turtle4j-ros2:latest
```

To run it:

```bash
docker run --rm -it -p 127.0.0.1:6080:6080 -p 127.0.0.1:9090:9090 ghcr.io/javiergs/turtle4j-ros2:latest
```

**You can skip the separate `docker pull` command.** When you run `docker run`, Docker automatically downloads the image if it is not already on your computer. The first download can take time; later starts reuse the downloaded image. To explicitly fetch a newer `latest` image, use `docker pull` again.

Keep the terminal open and continue to **Open the desktop**.

## Method 2 — Build from downloaded files (works now)

You do **not** need Git or a repository clone.

1. Go to the [repository home page](https://github.com/javiergs/turtle4j-getting-started).
2. Click **Code → Download ZIP**.
3. Extract the ZIP and open its **docker** folder.
4. Keep the following files together: `Dockerfile` (build instructions), `compose.yaml` (run configuration), `supervisord.conf` (starts services), and `turtle4j-wallpaper.png` (desktop background). The wallpaper is automatically included during the build.
5. Open a terminal **in the extracted docker folder**. You can navigate there with `cd` followed by the folder path.
6. Run:

```bash
docker compose up --build
```

Docker now downloads the required software and builds the environment. **The first build may take several minutes or longer.** Leave this terminal running.

**Difference between the options:** Method 1 downloads an image that has already been built. Method 2 builds that image locally from the supplied files. Both provide the same type of environment.

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
