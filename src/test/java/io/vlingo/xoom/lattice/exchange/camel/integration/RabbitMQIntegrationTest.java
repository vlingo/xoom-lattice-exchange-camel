// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.integration;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.testcontainers.containers.GenericContainer;

import io.vlingo.xoom.lattice.exchange.camel.CamelTestWithDockerIntegration;

// This is a *really* slow test because RabbitMQ takes it's time to bootstrap (around 40s in my machine)
@SuppressWarnings("rawtypes")
public class RabbitMQIntegrationTest extends CamelTestWithDockerIntegration {
    private static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Override
    @SuppressWarnings("resource")
    protected GenericContainer testContainer() {
        return new GenericContainer("rabbitmq:latest")
                .withExposedPorts(5672);
    }

    @Override
    protected String exchangeUri(GenericContainer rabbitMQ) {
        camelRegistry().bind("rabbitConnectionFactory",
                new CachingConnectionFactory(rabbitMQ.getHost(), rabbitMQ.getMappedPort(5672)));
        return String.format("spring-rabbitmq:%s?connectionFactory=#rabbitConnectionFactory&autoDeclare=true", QUEUE_NAME);
    }
}
