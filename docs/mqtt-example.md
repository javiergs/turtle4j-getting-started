# MQTT publish/subscribe example

The `MqttPublishSubscribe` example requires **TURTLE4J 1.1.0**, which adds optional MQTT support.

With TURTLE4J 1.1.0 published to Maven Central, run from the repository root with rosbridge running:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=examples.MqttPublishSubscribe
```

The example connects to rosbridge at `ws://localhost:9090` and MQTT at `tcp://broker.hivemq.com:1883` by default. Both endpoints can be passed as command-line arguments (rosbridge first, MQTT second). It subscribes for ten seconds before closing.

The public MQTT broker is for classroom experimentation only, not physical robot control. The example intentionally does not issue a velocity command.
