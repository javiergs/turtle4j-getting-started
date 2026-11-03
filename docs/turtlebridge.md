# TurtleBridge — MQTT ↔ ROS 2 without writing Java

> **Available now:** The TurtleBridge Docker image is published. Native desktop installers are not yet available; use the Docker instructions below.

TurtleBridge is a separate application that connects an MQTT broker to ROS 2 through rosbridge. Unlike the Turtle4J Java examples, TurtleBridge does **not** require you to install Java, Maven, or the private source code. It is designed for learning with **Turtlesim**, not for controlling physical robots.

## 1. Use TurtleBridge in Docker (recommended for beginners)

The published Docker image includes the [ROS 2 Turtlesim Desktop](../docker/README.md) plus TurtleBridge. You do not need to install Java, Maven, or a separate TurtleBridge executable.

First, [install and start Docker](../docker/README.md#step-1--install-and-start-docker). Then, in Terminal or PowerShell, download the image:

```bash
docker pull ghcr.io/jgs-lab/turtlebridge-ros2-simulator:latest
```

**macOS/Linux:**

```bash
docker run --rm -it --shm-size=1g -e DISPLAY=:1 \
  -p 127.0.0.1:6080:6080 -p 127.0.0.1:9090:9090 \
  ghcr.io/jgs-lab/turtlebridge-ros2-simulator:latest
```

**Windows PowerShell:**

```powershell
docker run --rm -it --shm-size=1g -e DISPLAY=:1 `
  -p 127.0.0.1:6080:6080 -p 127.0.0.1:9090:9090 `
  ghcr.io/jgs-lab/turtlebridge-ros2-simulator:latest
```

Next, [open the Linux desktop and launch Turtlesim](../docker/README.md#step-3--open-the-linux-desktop). TurtleBridge starts automatically in this image. Its configuration is stored inside the container at `/root/.turtlebridge/config.json`; the MQTT prefix is randomly generated for each new container. To inspect it, open another terminal and run `docker ps`, then `docker exec <container-id> cat /root/.turtlebridge/config.json`.

**Important:** The separate desktop installers below are not yet verified releases. Use the Docker image for the current student tutorial.

## 2. Native TurtleBridge installers (planned)

Installers will be provided on the public [GitHub Releases page](https://github.com/javiergs/turtle4j-getting-started/releases). **There are no TurtleBridge installers available here yet.** Do not download installers from unofficial sources.

| Computer | Planned package | Installation after release |
| --- | --- | --- |
| macOS, Apple Silicon (M-series) | `.dmg` (ARM64) | Open the disk image and copy TurtleBridge into Applications. |
| macOS, Intel | `.dmg` (x64) | Open the disk image and copy TurtleBridge into Applications. |
| Windows, 64-bit | `.exe` installer | Run the installer and follow its prompts. |
| Ubuntu/Debian Linux, AMD64 | `.deb` | Install using your distribution's package manager. |

Each installer bundles the Java runtime used by TurtleBridge. Windows ARM64, Linux ARM64, and non-Debian Linux distributions are not currently supported by these packages.

**Security notices:** Initial unsigned or unnotarized builds may trigger macOS Gatekeeper or Windows SmartScreen warnings. Do not disable system-wide security protections or bypass warnings for an unverified installer. Ask your instructor for a verified release and installation guidance.

## 3. Native application configuration (planned)

TurtleBridge creates a configuration file when started without arguments:

- **macOS/Linux:** `~/.turtlebridge/config.json`
- **Windows:** `%USERPROFILE%\.turtlebridge\config.json`

The file includes the default rosbridge address (`ws://localhost:9090`), a public HiveMQ MQTT broker, and a **random MQTT topic prefix** to reduce collisions between students.

TurtleBridge is currently a **command-line application**. It does not provide a graphical configuration editor or connection-status window. Platform-specific launcher commands and a verified first-run walkthrough will be added after installed-application testing. A desktop icon alone is not evidence that a background bridge is running.

Command-line options:

```text
TurtleBridge --help
TurtleBridge --init
TurtleBridge --config <path-to-config.json>
```

- `--help` displays available commands without connecting.
- `--init` creates the default configuration without connecting; it does not overwrite an existing file.
- `--config` loads a specified JSON configuration file.
- Running without arguments creates or reuses the default configuration and attempts to connect.

The exact installed executable path depends on your operating system and will be confirmed before release.

## 4. Understand the data flow

```text
MQTT publisher → MQTT broker → TurtleBridge → rosbridge → Turtlesim
Turtlesim → rosbridge → TurtleBridge → MQTT broker → MQTT subscriber
```

The default example maps MQTT velocity commands to the ROS 2 topic `/turtle1/cmd_vel` and forwards `/turtle1/pose` telemetry to MQTT.

**Important:** The default HiveMQ broker is public. Do not send passwords, personal information, or sensitive research data. Random topic names are **not** authentication. Use the configuration only with a simulator; do not connect it to a physical robot.

## 5. Troubleshooting

- **Connection refused:** Start Docker, launch Turtlesim, and verify that rosbridge is accessible on port 9090.
- **No movement:** Check that the bridge is running and your MQTT publisher uses the topic prefix from your configuration.
- **No pose messages:** Verify that Turtlesim is running and the ROS 2 `/turtle1/pose` topic is available.
- **MQTT connection fails:** Verify internet access to the public broker or configure a broker you control.
- **Security warning on installer:** Stop and check with the instructor; do not disable system-wide protections.

## Release checklist (maintainers)

Before removing the preview notice and adding direct downloads:

1. Verify the **actual installed** Windows EXE, Linux DEB, and both macOS DMGs on clean machines.
2. Confirm that the installed launcher works without a separate Java installation.
3. Confirm `--init`, configuration persistence, and successful ROS 2/MQTT communication after installation.
4. Document the exact native launcher paths, supported OS versions, and signing/notarization status.
5. Publish only the reviewed obfuscated binaries, their checksums, and release notes. **Do not publish the private library or bridge source code.**
6. Replace the preview notice with a versioned release link only after the above checks pass.
