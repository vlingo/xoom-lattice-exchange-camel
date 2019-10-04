package com.github.kmruiz.lattice.camel.integration;

import com.github.kmruiz.lattice.camel.CamelTestWithDockerIntegration;
import org.testcontainers.containers.GenericContainer;

import java.util.UUID;

public class ActiveMQIntegrationTest extends CamelTestWithDockerIntegration {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    protected GenericContainer testContainer() {
        return new GenericContainer("rmohr/activemq:5.15.9-alpine")
                .withExposedPorts(61616);
    }

    @Override
    protected String exchangeUri(GenericContainer activeMQ) {
        return String.format("activemq:queue:%s?brokerURL=auto://%s:%d", QUEUE_NAME, activeMQ.getContainerIpAddress(), activeMQ.getMappedPort(61616));
    }
}
