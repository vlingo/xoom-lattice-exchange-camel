package io.vlingo.xoom.lattice.exchange.camel.implnative;

import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.plugin.logging.slf4j.Slf4jLoggerPlugin;
import io.vlingo.xoom.cluster.ClusterProperties;
import io.vlingo.xoom.lattice.grid.Grid;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultConsumerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.support.DefaultRegistry;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public final class NativeBuildEntryPoint {
  @CEntryPoint(name = "Java_io_vlingo_xoom_lattice_exchange_camelnative_Native_start")
  public static int start(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer name) {
    final String nameString = CTypeConversion.toJavaString(name);
    Configuration configuration =
        Configuration
            .define()
            .with(Slf4jLoggerPlugin
                .Slf4jLoggerPluginConfiguration
                .define()
                .defaultLogger()
                .name("xoom-actors"));
    World world = World.start(nameString, configuration).world();

    final io.vlingo.xoom.cluster.model.Properties properties = ClusterProperties.oneNode();

    try {
      Grid.start(world, properties, "node1").quorumAchieved();
      CamelContext context = new DefaultCamelContext(new DefaultRegistry());
      new DefaultProducerTemplate(context);
      new DefaultConsumerTemplate(context);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
