# MQTT publish/subscribe example (development API)

The `MqttPublishSubscribe` example uses MQTT methods added to TURTLE4J after its original Maven Central 1.0.0 release. The published 1.0.0 artifact does **not** provide these methods.

Until a new TURTLE4J release is published, build the current library source into your local Maven repository:

```bash
git clone https://github.com/jgs-lab/api-turtle4j.git
mvn -f api-turtle4j/pom.xml clean install

git clone https://github.com/javiergs/turtle4j-getting-started.git
cd turtle4j-getting-started
mvn compile
mvn exec:java -Dexec.mainClass=examples.MqttPublishSubscribe
```

The example connects to rosbridge at `ws://localhost:9090` and MQTT at `tcp://broker.hivemq.com:1883` by default. Both endpoints can be passed as command-line arguments (rosbridge first, MQTT second). It subscribes for ten seconds before closing. Start rosbridge before running it.

This uses a public broker for classroom experimentation only. Do **not** use an unauthenticated public MQTT broker to control physical robots. The example intentionally does not issue a velocity command.

The PR's CI installs the current TURTLE4J source before compiling, rather than relying on the older published artifact. Once a compatible version is released, remove that CI workaround and update the Maven dependency.
