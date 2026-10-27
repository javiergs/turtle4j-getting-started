# TURTLE4J Getting Started

Runnable examples and setup guides for **TURTLE4J**, a Java 21 client for ROS 2 through rosbridge.

## First example: connect to rosbridge

Requires Java 21+, Maven, and a running rosbridge server. The default endpoint is `ws://localhost:9090` (the optional Turtlesim Docker environment will be migrated separately).

```bash
mvn compile
mvn exec:java -Dexec.mainClass=examples.ConnectRobot
```

To use a different endpoint, pass it as an argument:

```bash
mvn exec:java -Dexec.mainClass=examples.ConnectRobot -Dexec.args=ws://ROBOT_IP:9090
```

This project depends on the **published Maven Central artifact** `com.javiergs:turtle4j:1.0.0`; it does not compile the private library's source code. A reachable rosbridge endpoint is still required.

## API documentation

- [TURTLE4J 1.0.0 Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.0.0/) — frozen release reference.
- [Development Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/) — automatically refreshed through reviewed pull requests.

See the [documentation maintenance guide](docs/javadoc/README.md) for details.
