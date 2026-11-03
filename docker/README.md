# Docker Setup for Turtlesim 2D Environment

This tutorial uses Docker to create a virtual Linux desktop running Ubuntu 22.04 and ROS 2 Humble. The environment includes Turtlesim, a simple 2D robot simulator, and rosbridge, which allows external applications such as Turtle4J to communicate with ROS 2 through WebSockets. You can access the Linux desktop directly from your web browser using noVNC.

<img width="80%" alt="turtle docker" src="https://github.com/user-attachments/assets/5d739a90-be13-4c90-a975-5be11e8bd5e7" />

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

## Step 2 — Get the simulation environment

Docker uses an **image** (a package containing the software) to start a **container** (the running environment). You can either download an image someone has already built or build it yourself. **Choose one of the following methods:** Method 1 downloads the published prebuilt image and is the simplest option, Method 2 builds the image on your computer from the provided files.

### Method 1 — Download the Docker image

Open Terminal or PowerShell and download the image from GitHub Container Registry:

```bash
docker pull ghcr.io/javiergs/turtle4j-ros2:latest
```

Then start the container:

**macOS/Linux (Terminal):**

```bash
docker run --rm -it \
  --shm-size=1g \
  -e DISPLAY=:1 \
  -p 127.0.0.1:6080:6080 \
  -p 127.0.0.1:9090:9090 \
  ghcr.io/javiergs/turtle4j-ros2:latest
```

**Windows (PowerShell):**

```powershell
docker run --rm -it `
  --shm-size=1g `
  -e DISPLAY=:1 `
  -p 127.0.0.1:6080:6080 `
  -p 127.0.0.1:9090:9090 `
  ghcr.io/javiergs/turtle4j-ros2:latest
```

The image is built and published by the repository's GitHub Actions workflow. **If the download reports that the image cannot be found or access is denied**, the publishing workflow may not have completed or the package may not yet be public. In that case, use Method 2. Keep the terminal open while the container runs.

### Method 2 — Build the image from the provided Dockerfile

**1. Download the files.** Download `Dockerfile`, `compose.yaml`, `supervisord.conf`, and `turtle4j-wallpaper.png` from this folder. Save all four files in the same directory on your computer.

**2. Open a terminal in the `docker` folder.** For example, if you extracted the ZIP into Downloads on macOS or Linux:

```bash
cd ~/Downloads/turtle4j-getting-started-main/docker
```

On Windows, open the extracted `docker` folder in File Explorer, click the address bar, type `powershell`, and press Enter. This opens PowerShell in that folder.

**3. Build the image.** In that terminal, run:

```bash
docker build -t turtle4j-ros2:local .
```

The final `.` means “use the Dockerfile in the current folder.” Docker downloads and installs the required software. The first build may take several minutes or longer.

**4. Start the container.** After the build finishes, run:

**macOS/Linux (Terminal):**

```bash
docker run --rm -it \
  --shm-size=1g \
  -e DISPLAY=:1 \
  -p 127.0.0.1:6080:6080 \
  -p 127.0.0.1:9090:9090 \
  turtle4j-ros2:local
```

**Windows (PowerShell):**

```powershell
docker run --rm -it `
  --shm-size=1g `
  -e DISPLAY=:1 `
  -p 127.0.0.1:6080:6080 `
  -p 127.0.0.1:9090:9090 `
  turtle4j-ros2:local
```

Keep this terminal open while using the environment.

**Alternative:** The provided `compose.yaml` can build and start the environment in one command. Instead of steps 3 and 4, run the following **from the same `docker` folder**:

```bash
docker compose up --build
```

Do **not** run both approaches at the same time; they use the same network ports.

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
2. **[Getting Started with Turtle4J](../docs/getting-started.md)** — connect a Java application to the simulator through rosbridge at `ws://localhost:9090`.

The browser address `http://localhost:6080/vnc.html` opens the Linux desktop. The WebSocket address `ws://localhost:9090` is for Java-to-ROS communication.

## Stop the environment

Return to the terminal that started Docker and press **Ctrl+C**. If you used the Compose alternative, you can also run `docker compose down` from the `docker` folder. The `--rm` option in the other commands removes the stopped container, but keeps the downloaded or built image.

## Troubleshooting

- **Docker command not found:** Install Docker, then reopen your terminal.
- **Cannot connect to Docker:** Start Docker Desktop and wait for it to finish launching.
- **Port 6080 or 9090 already in use:** Stop the other application or container using that port.
- **Browser desktop unavailable:** Check that Docker is running and that the startup terminal remains open.
- **ROS 2 command does not move the turtle:** Start Turtlesim first, then run the command in a terminal inside the Linux desktop.
- **Method 1 says image not found or access denied:** Check your network connection and the package's availability; Method 2 builds the image locally.

