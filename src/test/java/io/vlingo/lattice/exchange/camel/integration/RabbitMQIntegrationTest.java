// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

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
