package io.vlingo.lattice.exchange.camel.integration;

import io.vlingo.lattice.exchange.camel.CamelTestWithDockerIntegration;
import org.testcontainers.containers.GenericContainer;

import java.util.UUID;

// This is a *really* slow test because RabbitMQ takes it's time to bootstrap (around 40s in my machine)
public class RabbitMQIntegrationTest extends CamelTestWithDockerIntegration {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    protected GenericContainer testContainer() {
        return new GenericContainer("rabbitmq:latest")
                .withExposedPorts(5672);
    }

    @Override
    protected String exchangeUri(GenericContainer rabbitMQ) {
        return String.format("rabbitmq:%s?addresses=%s:%s,", QUEUE_NAME, rabbitMQ.getContainerIpAddress(), rabbitMQ.getMappedPort(5672));
    }
}
